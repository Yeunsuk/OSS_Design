// ── 상태 ─────────────────────────────────────────────────────────────────────
let regionData      = null;
let overlays        = [];
let dongOverlays    = [];
let geocodeCache    = {};
let isDisplayOn     = true;
let currentSido     = '대구광역시';
let currentDistrict = null;
let districtCoords  = DISTRICT_COORDS[currentSido];

// ── 지도 초기화 ───────────────────────────────────────────────────────────────
const { lat, lng, level } = SIDO_CENTERS[currentSido];
const map = new kakao.maps.Map(document.getElementById('map'), {
    center: new kakao.maps.LatLng(lat, lng),
    level,
});

// ── Discord 에러 알림 ─────────────────────────────────────────────────────────
// 프론트엔드 오류 발생 시 Discord 웹훅으로 알림 전송
function notifyFrontendError(context, error) {
    const webhookUrl = DISCORD_WEBHOOK_URL_ERR;
    if (!webhookUrl) return;

    const message = `🌐 [프론트] ${context}\\n원인: ${String(error).replace(/"/g, "'")}`;
    fetch(webhookUrl, {
        method:  'POST',
        headers: { 'Content-Type': 'application/json' },
        body:    JSON.stringify({ content: message }),
    }).catch(() => {}); // 웹훅 실패가 UI를 막으면 안 됨
}

// ── 지도 이벤트 ───────────────────────────────────────────────────────────────
// 줌 변경 시 임계값 기준으로 구/군 ↔ 동 오버레이 전환
kakao.maps.event.addListener(map, 'zoom_changed', () => {
    if (map.getLevel() <= DONG_ZOOM_THRESHOLD) {
        overlays.forEach(o => o.setMap(null));
        switchDistrict(findClosestDistrict(map.getCenter()));
    } else {
        currentDistrict = null;
        hideDongOverlays();
        if (isDisplayOn) renderOverlays();
    }
});

// 지도 중심 이동 시 가장 가까운 구로 동 오버레이 갱신
kakao.maps.event.addListener(map, 'center_changed', () => {
    if (map.getLevel() > DONG_ZOOM_THRESHOLD) return;
    switchDistrict(findClosestDistrict(map.getCenter()));
});

// 현재 구가 바뀐 경우에만 동 오버레이를 다시 렌더링
function switchDistrict(name) {
    if (name === currentDistrict) return;
    currentDistrict = name;
    renderDongOverlays(name);
}

// ── 데이터 로드 ───────────────────────────────────────────────────────────────
fetch('./data/regions.json')
    .then(res => {
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return res.json();
    })
    .then(data => { regionData = data; renderOverlays(); })
    .catch(err => {
        console.error('데이터 로드 실패:', err);
        notifyFrontendError('regions.json 로드 실패', err);
    });

// ── 점수 계산 ─────────────────────────────────────────────────────────────────
// 4개 지표에 가중치를 적용해 최종 점수(0~100) 계산
function calcFinalScore(indicators, weights) {
    const { crime: wc, medical: wm, school: ws, convenience: wv } = weights;
    const total = wc + wm + ws + wv;
    return Math.round((
        indicators.crime_count.score       * wc +
        indicators.medical_count.score     * wm +
        indicators.school_count.score      * ws +
        indicators.convenience_count.score * wv
    ) / total * 10) / 10;
}

// 점수 구간에 따라 CSS 클래스명 반환 (high/mid/low)
function scoreClass(score) {
    if (score >= 80) return 'score-high';
    if (score >= 50) return 'score-mid';
    return 'score-low';
}

// 가중치 입력 필드에서 현재 가중치 값을 읽어 객체로 반환
function getWeights() {
    const get = id => parseInt(document.getElementById(id).value) || 3;
    return {
        crime:       get('w_crime'),
        medical:     get('w_medical'),
        school:      get('w_school'),
        convenience: get('w_convenience'),
    };
}

// ── 오버레이 공통 헬퍼 ────────────────────────────────────────────────────────
// 지역명·점수·지표 아이콘이 담긴 오버레이 HTML 문자열 생성
function buildOverlayHTML(name, indicators, weights, onClickAttr) {
    const score = calcFinalScore(indicators, weights);
    const cls   = scoreClass(score);
    return `
        <div class="overlay-wrap" onclick="${onClickAttr}">
            <div class="overlay-name">${name}</div>
            <div class="overlay-score ${cls}">${score}</div>
            <div class="overlay-detail">
                👮 ${indicators.crime_count.score.toFixed(0)}
                🏥 ${indicators.medical_count.score.toFixed(0)}
                🏫 ${indicators.school_count.score.toFixed(0)}
                🏪 ${indicators.convenience_count.score.toFixed(0)}
            </div>
        </div>`;
}

// 주어진 좌표에 카카오맵 커스텀 오버레이 객체 생성
function createOverlay(content, coord) {
    return new kakao.maps.CustomOverlay({
        position: new kakao.maps.LatLng(coord.lat, coord.lng),
        content,
        yAnchor: 1.0,
        zIndex:  3,
    });
}

// ── 상세 모달 공통 헬퍼 ───────────────────────────────────────────────────────
// 상세 모달 내 지표 행(라벨 + 값) HTML 생성
function buildDetailRow(label, value) {
    return `
        <div class="detail-row">
            <span class="detail-label">${label}</span>
            <span class="detail-value">${value}</span>
        </div>`;
}

// 구/동 클릭 시 종합 점수 및 지표별 상세 정보를 모달로 표시
function showDetailModal(title, indicators, weights, isCrimeByDistrict = false, districtName = '') {
    const ind   = indicators;
    const score = calcFinalScore(ind, weights);
    const cls   = scoreClass(score);

    document.getElementById('detailTitle').innerHTML = `📍 ${title}`;
    document.getElementById('detailBody').innerHTML  = `
        <div class="detail-score ${cls}">${score}점</div>
        <div class="detail-total">종합 점수 (가중치 적용)</div>
        ${buildDetailRow('👮 범죄 발생',
            isCrimeByDistrict
                ? `${districtName} 기준 · ${ind.crime_count.score.toFixed(1)}점`
                : `${ind.crime_count.value.toLocaleString()}건 · ${ind.crime_count.score.toFixed(1)}점`
        )}
        ${buildDetailRow('🏥 의료시설', `${ind.medical_count.value.toLocaleString()}개 · ${ind.medical_count.score.toFixed(1)}점`)}
        ${buildDetailRow('🏫 학교',     `${ind.school_count.value.toLocaleString()}개 · ${ind.school_count.score.toFixed(1)}점`)}
        ${buildDetailRow('🏪 편의시설', `${ind.convenience_count.value.toLocaleString()}개 · ${ind.convenience_count.score.toFixed(1)}점`)}
    `;
    openModal('detailModal');
}

// ── 구/군 오버레이 ────────────────────────────────────────────────────────────
// 현재 시/도의 모든 구/군 오버레이를 지우고 다시 그림
function renderOverlays() {
    if (!regionData) return;
    overlays.forEach(o => o.setMap(null));
    overlays = [];

    try {
        const weights   = getWeights();
        const districts = regionData.regions[currentSido].districts;

        Object.entries(districts).forEach(([name, data]) => {
            const coord = districtCoords[name];
            if (!coord) return;

            const overlay = createOverlay(
                buildOverlayHTML(name, data.indicators, weights, `onOverlayClick('${name}')`),
                coord
            );
            overlay.setMap(map);
            overlays.push(overlay);
        });
    } catch (e) {
        console.error('오버레이 렌더링 실패:', e);
        notifyFrontendError(`구/군 오버레이 렌더링 실패 (${currentSido})`, e);
    }
}

// 구/군 오버레이 클릭 시 해당 구 상세 모달 표시
function onOverlayClick(name) {
    const ind = regionData.regions[currentSido].districts[name].indicators;
    showDetailModal(name, ind, getWeights());
}

// ── 동 오버레이 ───────────────────────────────────────────────────────────────
// 특정 구의 모든 동을 지오코딩 후 오버레이 배치 (캐시 활용)
function renderDongOverlays(districtName) {
    hideDongOverlays();
    if (!regionData) return;

    const districtData = regionData.regions[currentSido].districts[districtName];
    if (!districtData?.dongs) return;

    const weights   = getWeights();
    const geocoder  = new kakao.maps.services.Geocoder();
    const sidoShort = currentSido.replace('광역시', '').replace('특별시', '');

    Object.entries(districtData.dongs).forEach(([dongName, dongData]) => {
        const addr = `${sidoShort} ${districtName} ${dongName}`;

        if (geocodeCache[addr]) {
            placeDongOverlay(districtName, dongName, dongData, geocodeCache[addr], weights);
            return;
        }

        geocoder.addressSearch(addr, (result, status) => {
            if (status !== kakao.maps.services.Status.OK) {
                notifyFrontendError(`지오코딩 실패: ${addr}`, `status=${status}`);
                return;
            }
            const coord = { lat: parseFloat(result[0].y), lng: parseFloat(result[0].x) };
            geocodeCache[addr] = coord;
            placeDongOverlay(districtName, dongName, dongData, coord, weights);
        });
    });
}

// 동 오버레이 1개를 지도에 추가 (표시 OFF 상태면 건너뜀)
function placeDongOverlay(districtName, dongName, dongData, coord, weights) {
    if (!isDisplayOn) return;
    const overlay = createOverlay(
        buildOverlayHTML(dongName, dongData.indicators, weights, `onDongClick('${districtName}','${dongName}')`),
        coord
    );
    overlay.setMap(map);
    dongOverlays.push(overlay);
}

// 현재 표시된 모든 동 오버레이를 제거
function hideDongOverlays() {
    dongOverlays.forEach(o => o.setMap(null));
    dongOverlays = [];
}

// 동 오버레이 클릭 시 해당 동 상세 모달 표시 (범죄는 구 기준으로 표시)
function onDongClick(districtName, dongName) {
    const ind = regionData.regions[currentSido].districts[districtName].dongs[dongName].indicators;
    showDetailModal(`${districtName} ${dongName}`, ind, getWeights(), true, districtName);
}

// ── 구 탐색 ───────────────────────────────────────────────────────────────────
// 지도 중심에서 가장 가까운 구/군 이름을 반환 (유클리드 거리)
function findClosestDistrict(center) {
    let minDist = Infinity, closest = null;
    Object.entries(districtCoords).forEach(([name, coord]) => {
        const dist = (center.getLat() - coord.lat) ** 2
                   + (center.getLng() - coord.lng) ** 2;
        if (dist < minDist) { minDist = dist; closest = name; }
    });
    return closest;
}

// ── UI 컨트롤 ─────────────────────────────────────────────────────────────────
// 오버레이 표시 ON/OFF 토글 및 버튼 상태 반영
function toggleDisplay() {
    isDisplayOn = !isDisplayOn;
    const btn = document.getElementById('toggleBtn');
    [...overlays, ...dongOverlays].forEach(o => o.setMap(isDisplayOn ? map : null));
    btn.innerText = isDisplayOn ? '🔔 표시 ON' : '🔕 표시 OFF';
    btn.classList.toggle('active', !isDisplayOn);
}

// 시/도 선택 시 지도 중심·줌 이동 후 오버레이 재렌더링
function selectSido(sido) {
    closeModal('sidoModal');
    currentSido     = sido;
    districtCoords  = DISTRICT_COORDS[sido];
    currentDistrict = null;
    hideDongOverlays();

    const center = SIDO_CENTERS[sido];
    map.setCenter(new kakao.maps.LatLng(center.lat, center.lng));
    map.setLevel(center.level);

    if (isDisplayOn) renderOverlays();
}

// 가중치 모달 닫고 변경된 가중치로 오버레이 재렌더링
function applyWeights() {
    closeModal('weightModal');
    renderOverlays();
}

// 지정 id의 모달 열기/닫기
function openModal(id)  { document.getElementById(id).style.display = 'block'; }
function closeModal(id) { document.getElementById(id).style.display = 'none';  }
import java.util.*;

/**
 * 알림:
 *   - 수집기 1개 실패 → 에러 웹훅 전송 후 다음 수집기 계속 진행
 *   - 시/도 전체 완료 → 성공 웹훅 전송
 *   - 프로그램 자체 크래시 → 치명적 에러 웹훅 전송
 */
public class CollectorMain {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  살기좋은 주거지 데이터 수집 시작");
        System.out.println("========================================\n");

        String[] sidos = { "대구광역시", "부산광역시", "서울특별시" };

        // 수집기 목록 - 새 수집기는 여기에만 추가하면 됩니다
        List<Collector> collectors = Arrays.asList(
            new CollectorSafety(),
            new CollectorInfra(),
            new CollectorSchool(),
            new CollectorConvenience()
        );

        try {
            for (String sido : sidos) {
                System.out.println("\n======== " + sido + " ========");
                runSido(sido, collectors);
            }
            System.out.println("\n전체 완료!");

        } catch (Exception e) {
            // CollectorMain 자체의 예상 못한 크래시
            System.err.println("치명적 오류: " + e.getMessage());
            Discord.notifyFatal(e);
        }
    }

    private static void runSido(String sido, List<Collector> collectors) {
        RegionStore ds   = new RegionStore(sido);
        boolean hasError     = false;
        int step             = 1;

        for (Collector collector : collectors) {
            String name = collector.getClass().getSimpleName();
            System.out.printf("\n[%d/%d] %s 수집 중...%n", step++, collectors.size(), name);

            try {
                collector.collect(ds);
            } catch (Exception e) {
                // 수집기 1개 실패 → 에러 알림 후 나머지 수집기는 계속 진행
                System.err.println("  [오류] " + name + ": " + e.getMessage());
                Discord.notifyError(sido, name, e);
                hasError = true;
            }
        }

        try {
            ScoreCalculator.calculate(ds);
            System.out.println("\n저장 중...");
            ds.save();
        } catch (Exception e) {
            System.err.println("저장 실패: " + e.getMessage());
            Discord.notifyError(sido, "저장/점수계산", e);
            hasError = true;
        }

        // 에러가 하나도 없을 때
        if (!hasError) {
            //Discord.notifySuccess(sido);
        }
    }
}
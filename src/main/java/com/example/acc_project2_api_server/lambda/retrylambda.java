//public class RetryLambdaHandler implements RequestHandler<List<KafkaEvent>, Void> {
//
//    private final WebClient webClient = WebClient.builder()
//            .baseUrl("https://api.example.com")  // Spring API Gateway/ALB URL
//            .build();
//
//    @Override
//    public Void handleRequest(List<KafkaEvent> records, Context context) {
//        for (KafkaEvent record : records) {
//            FailureEvent ev = parseFailureEvent(record);
//            webClient.post()
//                    .uri("/retries")
//                    .bodyValue(ev)
//                    .retrieve()
//                    .toBodilessEntity()
//                    .block();
//        }
//        return null;
//    }
//
//    private FailureEvent parseFailureEvent(KafkaEvent record) {
//        // record.value() 에 JSON 직렬화된 FailureEvent가 담겨 있다고 가정
//        return new ObjectMapper().readValue(record.getValue(), FailureEvent.class);
//    }
//}

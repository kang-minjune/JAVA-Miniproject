import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeliveryGoods {
	private static ShoppingManager shopManager = new ShoppingManager(); // ShoppingManager 객체 생성
	private static final String PURCHASE_FILE = "Purchase.csv"; // 주문서 파일
	private static final String DELIVER_FILE = "deliverInfo.csv"; // 배송정보 파일
	private static String search = "";
    public static void DeliveryGoodsInfo() throws IOException {
	    	try {
	    		BufferedReader reader = new BufferedReader(new FileReader(PURCHASE_FILE));
	    		FileWriter writer = new FileWriter(DELIVER_FILE, true);
	    	    String line; // 불러온 파일을 데이터별로 분리해서 저장해 줄 변수
	    	    String[] parts = null; // line 변수에서 분리한 데이터를 받아줄 배열
	    	    reader.readLine(); // 불러온 파일의 데이터를 버퍼에 입력
	    	    while ((line = reader.readLine()) != null) { // 파일에 데이터가 존재하면 line 변수에 문자열로 데이터 저장
	    	        parts = line.split(","); // ,를 기준으로 해서 데이터를 분리하여 parts 배열에 저장
	    	    }
	    	    String deliverData = String.format("\n%s,%s,%s,%s,%s,%s,%s", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], "배송완료");
	    	    writer.append(deliverData);
	            writer.flush(); // 저장된 데이터를 버퍼에서 삭제
	            writer.close(); // 입력한 파일을 종료
	            reader.close(); // 불러온 파일 닫기
	    	} catch (IOException e) {
	    	    e.printStackTrace(); // 예외 발생시 오류 출력
	    	} catch (NullPointerException e) {
	    		System.err.println("현재 등록된 배송정보가 없습니다.");
	    		MainApplication.menu(false); // 주문서 파일이 비어있을 경우 다시 메뉴로
	    	}
	    	
	    	try {
	    		BufferedReader reader = new BufferedReader(new FileReader(DELIVER_FILE));
	    		String line;
	            reader.readLine(); // csv 첫 줄은 필드라 건너뜀
	            System.out.println("\n[배송 정보]\n");
	            while ((line = reader.readLine()) != null) {
	            	String[] parts = line.split(",");
	                    //배송정보 출력 기능
	                    if (parts.length >= 7) {
	                        System.out.println("아이디: " + parts[0]);
	                        System.out.println("이름: " + parts[1]);
	                        System.out.println("상품명: " + parts[2]);
	                        System.out.println("가격: " + parts[3]);
	                        System.out.println("주소: " + parts[4]);
	                        System.out.println("배송 시작일: " + parts[5]);
	                        System.out.println("배송 상태: " + parts[6]);

	                        System.out.println();

	                        //송장번호 출력 기능 구현
	                        System.out.print("송장번호: ");

	                        for (int i = 0; i < 12; i++) {
	                            int dlNum = (int) (Math.random() * 9) + 1;
	                            System.out.print(dlNum);
	                        }

	                        //배송비 계산 출력 기능 구현
	                        int deliveryPrice = 3000;
	                        String deliveryAddress = parts[4];

	                        System.out.println();
	                        // 주소가 제주도일 경우 배송비가 5,000원
	                        if (deliveryAddress.equals("제주도")) {
	                            deliveryPrice = deliveryPrice + 5000;
	                            System.out.println("배송비: " + deliveryPrice + "원");
	                        } else System.out.println("배송비: " + deliveryPrice + "원");

	                        System.out.println("---------------------");
	                    } else {
	                        System.out.println("잘못된 형식의 데이터입니다.");
	                    }
	                }
	                System.out.println();
	                reader.close();
	            } catch (FileNotFoundException e) {
	                System.out.println("파일을 찾을 수 없습니다.");
	            } catch (IOException e) {
	                System.out.println("파일을 읽는 도중 오류가 발생했습니다.");
	            }
    }  	
}  


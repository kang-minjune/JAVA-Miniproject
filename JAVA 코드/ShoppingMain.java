import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class ShoppingMain extends Thread {
    private static final String MEMBER_FILE = "Member.csv"; // 회원 파일
    private static ShoppingManager shopManager = new ShoppingManager(); // ShoppingManager 객체 생성
    
    // 현재 날짜 출력
    private static LocalDateTime currentDateTime = LocalDateTime.now();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String dateTimeString = currentDateTime.format(formatter);
    
    public static void shopping() throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String item = ""; // 상품 이름
        String price = ""; // 상품 가격
        String name = ""; // 회원 이름
        String id = ""; // 회원 아이디
        String address = ""; // 회원 주소
        String changes = ""; // 상품 결제 후 카드 잔액
        int credit = 0; // MEMBER_FILE에 저장된 카드 값을 받아 줄 변수
        
        // 상품의 이름과 가격 입력
        itemLoop :
        while (true) {
            System.out.println("구매하실 상품과 가격을 입력해 주세요");
            shopManager.list(); //상품 목록 출력
            StringTokenizer st = new StringTokenizer(br.readLine());
            
            // 상품, 가격 입력이 없을 경우 itemLoop로 이동
            if (!st.hasMoreTokens()) {
                System.out.println("상품과 가격을 입력해 주세요");
                continue itemLoop;
            }
            
            item = st.nextToken(); // 상품 입력
            
            if (!st.hasMoreTokens()) {
                System.out.println("상품과 가격을 입력해 주세요");
                continue itemLoop;
            }
            
            price = st.nextToken(); // 가격 입력
            
            // 입력한 상품이 리스트에 있는지 확인
            if (shopManager.checkItem(item)) {
                System.out.println("상품을 선택하였습니다. 결제 메뉴로 이동합니다.\n");
                break itemLoop;
            } else {
                System.out.println("입력하신 상품은 리스트에 없습니다. 다시 입력해 주십시오.");
                continue itemLoop;
            }
        }
        
        // 주문서 작성(회원 아이디, 이름, 주소)
        purchaseLoop :
        while (true) {
            System.out.println("구매하실 상품을 선택하셨습니다. 주문서를 작성해 주세요");
            System.out.println("아이디, 이름, 주소를 입력해 주세요");
            StringTokenizer st = new StringTokenizer(br.readLine()); // 아이디, 이름, 주소를 Token을 받아서 입력

            // 아이디, 이름, 주소 입력이 없을 경우 purchaseLoop로 이동
            if (!st.hasMoreTokens()) {
                System.out.println("아이디, 이름, 주소를 입력해 주세요");
                continue purchaseLoop;
            }

            id = st.nextToken(); // 현재 로그인한 회원의 아이디 입력
            
            if (!st.hasMoreTokens()) {
                System.out.println("아이디, 이름, 주소를 입력해 주세요");
                continue purchaseLoop;
            }
            
            name = st.nextToken(); // 현재 로그인한 회원의 이름 입력
            
            if (!st.hasMoreTokens()) {
                System.out.println("아이디, 이름, 주소를 입력해 주세요");
                continue purchaseLoop;
            }
            
            address = st.nextToken(); // 현재 로그인한 회원의 주소 입력
            
            // 현재 로그인한 사용자의 ID와 일치하지 않을 경우 purchaseLoop로 이동
            if (!shopManager.checkId(id)) {
                System.out.println("입력하신 정보는 현재 계정과 일치하지 않습니다. 다시 입력해 주세요.");
                continue purchaseLoop;
            }

            // 모든 정보 입력 완료 객체 생성
            Shopping customer = new Shopping(id, name, item, Integer.parseInt(price), address, dateTimeString);
            System.out.println("주문서 작성을 완료하였습니다. 결제 내역을 출력합니다.");
            if (customer.getId().equals(id)) {
            	shopManager.order(customer); // 입력한 데이터를 주문서 파일에 저장
                shopManager.orderCheck(id); // 현재 로그인한 사용자의 ID를 체크한 후 객체를 생성
            	shopManager.printMenuHead(); // 주문서 양식 출력
                System.out.println(customer); // 생성된 객체의 내용을 출력
            }
            
            // 현재 로그인한 사용자의 카드 잔액을 조회
            try {
                BufferedReader reader = new BufferedReader(new FileReader(MEMBER_FILE)); // 회원 파일 불러오기
                String line; // 불러온 파일을 데이터별로 분리해서 저장해 줄 변수
                while ((line = reader.readLine()) != null) {
                    String[] check = line.split(","); // ,를 기준으로 해서 데이터를 분리해 check 배열에 입력
                    if (check[0].equals(id)) { // 현재 로그인한 회원의 아이디를 조회
                        credit = Integer.parseInt(check[6]); // 카드 잔액 조회
                    }
                }
                // 상품의 가격 - 사용자의 카드 잔액
                changes = String.format("%,d", credit - shopManager.orderList.getPrice()); // 1,000 단위로 변경
                System.out.println("카드 잔액은 " + changes + "원 남았습니다."); // 결제한 카드 잔액
                reader.close(); // 불러온 회원 파일을 종료
            } catch (IOException e) {
                e.printStackTrace(); // 예외 발생시 오류 출력
            }
            break purchaseLoop; // 결제 완료, 배송 시작
        }
        
        System.out.println("결제가 완료되었습니다. 배송을 시작합니다...");
        shopManager.deliverItem(item); // 배송 시간 메서드 호출
    }
}


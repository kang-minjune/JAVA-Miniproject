import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ShoppingManager {
	private static final String GOODS_FILE = "Goods.csv"; // 상품 파일
	private static final String PURCHASE_FILE = "Purchase.csv"; // 주문서 파일
	private static final String MEMBER_FILE = "Member.csv"; // 고객 파일
	private static final String CSV_FILE = "deliverInfo.csv";
    private static final String TEMP_FILE = "temp.csv";

	// 오늘 날짜 출력
	private static LocalDateTime currentDateTime = LocalDateTime.now();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String dateTimeString = currentDateTime.format(formatter);
    
    public Shopping orderList; // 작성한 주문서를 객체로 저장
    
	public void list() { // 쇼핑몰 목록
		// 반환용 리스트 변수
        List<List<String>> ret = new ArrayList<>();
        // 입력 스트림 오브젝트 생성
        BufferedReader reader = null;
 
        try {
            // 대상 CSV 파일의 경로 설정
            reader = Files.newBufferedReader(Paths.get("Goods.csv"),Charset.forName("UTF-8"));
            // CSV파일에서 읽어들인 1행분의 데이터
            String line = "";

            while((line = reader.readLine()) != null) {
                // CSV 파일의 1행을 저장하는 리스트 변수
                List<String> tmpList = new ArrayList<>();
                
                String array[] = line.split(",");
                
                // 배열에서 리스트로 변환
                tmpList = Arrays.asList(array);
                
                // 리스트 내용 출력
                System.out.println(tmpList);
                
                // 반환용 리스트에 1행의 데이터를 저장
                ret.add(tmpList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // 파일을 존재하지 않을 경우 오류 출력
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생시 오류 출력
        } finally { 
            try{
                if(reader != null) { // 파일의 데이터가 존재할 경우 불러온 파일을 정상적으로 종료
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace(); // 예외 발생시 오류 출력
            }

        }
	}
    
    // 상품 리스트 체크
    public boolean checkItem(String item) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(GOODS_FILE)); // 상품 파일 불러오기
            String line; // 불러온 파일을 데이터별로 분리해서 저장해 줄 변수
            while ((line = reader.readLine()) != null) { // 불러온 파일의 데이터가 존재한다면 line 변수에 문자열로 데이터를 저장
                String[] check = line.split(","); // ,를 기준으로 해서 데이터를 분리하여 check 배열에 저장
                if (check[0].equals(item)) { // 상품 파일에 회원이 입력한 상품이 존재하는지 확인
                    reader.close(); // 불러온 파일을 종료
                    return true; // 상품이 리스트에 있음
                }
            }
            reader.close(); // 불러온 파일을 종료
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생시 오류 출력
        }
        return false; // 상품이 리스트에 없음
    }
    
    // 회원 정보 체크
    public boolean checkId(String id) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MEMBER_FILE)); // 회원 파일 불러오기
            String line; // 불러온 파일을 데이터별로 분리해서 저장해 줄 변수
            while ((line = reader.readLine()) != null) { // 불러온 파일의 데이터가 존재한다면 line 변수에 문자열로 데이터를 저장
                String[] check = line.split(","); // ,를 기준으로 해서 데이터를 분리하여 check 배열에 저장
                if (check[0].equals(id)) { // 회원 파일에 현재 로그인한 회원의 아이디가 존재하는지 확인
                    reader.close(); // 불러온 파일 종료
                    return true; // 이름이 회원 리스트에 있음
                }
            }
            reader.close(); // 불러온 파일 종료
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생시 오류 출력
        }
        return false; // 이름이 회원 리스트에 없음
    }
    
    
    // 상품 구매
    public void order(Shopping shop) {
        try {
            FileWriter writer = new FileWriter(PURCHASE_FILE, true);
            // 입력한 데이터를 주문서 파일에 저장
            String purchaseData = String.format("\n%s,%s,%s,%d,%s,%s", shop.getId(), shop.getName(), shop.getItemName(), shop.getPrice(), shop.getAddress(), dateTimeString);
            writer.append(purchaseData);
            writer.flush(); // 저장된 데이터를 버퍼에서 삭제
            writer.close(); // 입력한 파일을 종료
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생시 오류 출력
        }
    }
    
    // 회원의 ID 체크 후 작성한 주문서 최종 확인
    public boolean orderCheck(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PURCHASE_FILE))) { // 주문서 파일 불러오기
            String line; // 불러온 파일을 데이터별로 분리해서 저장해 줄 변수
            reader.readLine(); // 불러온 파일의 데이터를 버퍼에 입력
            while ((line = reader.readLine()) != null) { // 파일에 데이터가 존재하면 line 변수에 문자열로 데이터 저장
                String[] parts = line.split(","); // ,를 기준으로 해서 데이터를 분리하여 parts 배열에 저장
                if(parts.length<2) return false;
                if (parts[0].equals(id)) { // 현재 로그인한 회원의 아이디와 주문서 파일의 아이디가 일치하는지 확인
                // 아이디가 일치할 경우 작성한 주문서 내용대로 객체 생성    
                orderList = new Shopping(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], dateTimeString);
                return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 발생시 오류 출력
        } 
        return false;
    }
    
 // Progress 메소드: 주어진 시간(밀리세컨드)에 따라 진행 상태를 보여줌
 // Progress 메소드: 주어진 시간(밀리세컨드)에 따라 진행 상태를 보여줌
    public void progress(int milliseconds) {
        JFrame f = new JFrame("JProgressBar Sample");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = f.getContentPane();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("배송중");
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);
        f.setSize(300, 100);
        f.setVisible(true);

        int duration = milliseconds / 1000; // 밀리세컨드를 초로 변환
        int step = 100 / duration;
        for (int i = 0; i < duration; i++) {
            final int value = i * step;
            final int finalI = i;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setValue(value);
                    if (finalI == duration - 1) {
                        progressBar.setValue(100);
                        Border border = BorderFactory.createTitledBorder("배송완료");
                        progressBar.setBorder(border);
                    }
                }
            });
            try {
                Thread.sleep(1000); // 1초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    

    // deliverItem 메소드: 아이템에 따라 배송 시간을 설정하고 Progress 메소드를 호출
    public void deliverItem(String item) {
        switch (item) {
            case "티셔츠":
                deliverWithTime(10000); // 10초
                break;
            case "코트":
                deliverWithTime(15000); // 15초
                break;
            case "구두":
                deliverWithTime(20000); // 20초
                break;
            default:
                System.out.println("해당 상품은 리스트에 없습니다.");
                break;
        }
    }

    public void deliverWithTime(int deliveryTime) {
        Thread deliveryThread = new Thread(() -> {
            progress(deliveryTime); // Progress 메소드를 호출하여 배송 진행 상태를 보여줌
            System.out.println("\n배송완료");
        });
        deliveryThread.start();  
    }

    // 주문서 양식
    public void printMenuHead() {
    	System.out.print("----------------------------------------------------------------------------------------\n");
        System.out.printf("%10s %10s %10s %10s %10s %20s\n",
                "아이디",
                "이름",
                "상품",
                "결제 가격",
                "주소",
                "배송시작 시간");
        System.out.print("----------------------------------------------------------------------------------------\n");
    }
    
    
 
}

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Shopping {
	
	private String id; // 회원 아이디
	private String name; // 회원 이름
	private String address; // 회원 주소
	private String itemName; // 상품 이름
	private int price; // 상품 가격
    private String startDelivery; // 배송시작 시간
    
    // 현재 날짜와 시간 출력
    private static LocalDateTime currentDateTime = LocalDateTime.now();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String dateTimeString = currentDateTime.format(formatter);
   
    // 회원의 상품 결제 정보(생성자)
    public Shopping(String id, String name, String itemName, int price, String address, String startDelivery) {
    	this.id = id;
    	this.name = name;
    	this.itemName = itemName;
    	this.price = price;
    	this.address = address;
    	this.startDelivery = startDelivery;
    }
    
    // 회원 아이디(setter)
    public void setId(String id) {
    	this.id = id;
    }
    
    // 회원 이름(setter)
    public void setName(String name) {
    	this.name = name;
    }
    
    // 회원 주소(setter)
 	public void setAddress(String address) {
 		this.address = address;
 	}
    
    // 상품 이름(setter)
	public void setItemName(String itemName) {
        this.itemName = itemName;
    }

	// 상품 가격(setter)
    public void setPrice(int price) {
        this.price = price;
    }
    
    // 배송시작 시간(setter)
    public void setStartDelivery(String startDelivery) {
    	this.startDelivery = startDelivery;
    }
    
    // 회원 아이디(getter)
    public String getId() {
    	return id;
    }
    
    // 회원 이름(getter)
    public String getName() {
    	return name;
    }
    
    // 회원 주소(getter)
    public String getAddress() {
    	return address;
    }
    
    // 상품 이름(getter)
    public String getItemName() {
        return itemName;
    }
    
    // 상품 가격(getter)
    public int getPrice() {
        return price;
    }
    
    // 배송 시간(getter)
    public String getStartDelivery() {
    	return startDelivery;
    }
    
    
    // 회원이 작성한 주문서 출력
    @Override
    public String toString() {
    	 String strPrice = String.format("%,d", price);
    	 return "%11s %10s %10s %10s %10s %25s\n".formatted(id,name,itemName,strPrice,address,dateTimeString);
    }
}

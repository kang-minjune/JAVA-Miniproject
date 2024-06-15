public class User {
    private String id; // 회원 아이디
    private String password; // 회원 비밀번호
    private String name; // 회원 이름
    private String address; // 회원 주소
    private String phoneNumber; // 회원 전화번호
    private String email; // 회원 이메일 
    private int balance; // 회원 카드 값
    
    // 회원 정보(생성자)
    public User(String id, String password, String name, String address, String phoneNumber, String email,
                int balance) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.balance = balance;
    }
    
    // 회원 카드 값(getter)
    public int getBalance() {
        return balance;
    }
    
    // 회원 카드 값(setter)
    public void setBalance(int balance) {
        this.balance = balance;
    }
    
    // 회원 아이디(getter)
    public String getId() {
        return id;
    }
    
    // 회원 아이디(setter)
    public void setId(String id) {
        this.id = id;
    }

    // 회원 비밀번호(getter)
    public String getPassword() {
        return password;
    }

    // 회원 비밀번호(setter)
    public void setPassword(String password) {
        this.password = password;
    }

    // 회원 이름(getter)
    public String getName() {
        return name;
    }

    // 회원 이름(setter)
    public void setName(String name) {
        this.name = name;
    }

    // 회원 주소(getter)
    public String getAddress() {
        return address;
    }

    // 회원 주소(setter)
    public void setAddress(String address) {
        this.address = address;
    }

    // 회원 전화번호(getter)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // 회원 전화번호(setter)
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // 회원 이메일(getter)
    public String getEmail() {
        return email;
    }

    // 회원 이메일(setter(
    public void setEmail(String email) {
        this.email = email;
    }

    // 회원 정보 출력
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "아이디: " + id + " 이름: " + name + " 주소: " + address + "  연락처: " + phoneNumber + "  이메일: " + email + "  잔액: " + balance;
    }
}

import java.io.*;
import java.util.Scanner;

public class MainApplication extends Thread{
    private static UserManager userManager = new UserManager();
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 프로그램 종료");
            System.out.print("번호를 입력하세요: ");
            try {
                int n = Integer.parseInt(br.readLine());
                switch (n) {
                    case 1:
                        login();
                        break;
                    case 2:
                        signUp();
                        break;
                    case 0:
                        System.out.println("프로그램을 종료합니다.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("올바른 번호를 입력하세요.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("올바른 번호를 입력해주세요");
            }
        }
    }

    private static void login() throws IOException {
        System.out.print("아이디: ");
        String loginId = br.readLine();
        System.out.print("비밀번호: ");
        String loginPassword = br.readLine();
        if (userManager.login(loginId, loginPassword)) {
            System.out.println("로그인 성공!");
            boolean root = loginId.equals("root");
            menu(root);
        } else {
            System.err.println("로그인 실패. 사용자 이름 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    private static void signUp() throws IOException {
        boolean signupSuccess = false;
        while (!signupSuccess) {
            System.out.print("아이디: ");
            String signupId = br.readLine();
            if (userManager.checkDuplicateId(signupId)) {
                System.out.println("중복된 아이디입니다. 다른 아이디를 입력하세요.");
            } else {
                System.out.print("비밀번호: ");
                String signupPassword = br.readLine();
                System.out.print("이름: ");
                String signupName = br.readLine();
                System.out.print("주소: ");
                String signupAddr = br.readLine();
                System.out.print("전화번호: ");
                String signupPhoneNum = br.readLine();
                System.out.print("이메일: ");
                String signupEmail = br.readLine();
                System.out.print("잔액: ");
                int signupBalance = Integer.parseInt(br.readLine());
                User newUser = new User(signupId, signupPassword, signupName, signupAddr, signupPhoneNum, signupEmail, signupBalance);
                userManager.signup(newUser);
                signupSuccess = true;
            }
        }
    }

    public static void menu(boolean root) throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("1. 쇼핑몰\n2. 배송정보\n3. 회원정보\n4. 로그아웃\n0. 프로그램 종료");
            System.out.print("번호를 입력하세요: ");
            int n = sc.nextInt();
            switch (n) {
                case 1:
                    System.out.println("쇼핑몰");
                    ShoppingMain.shopping();
                    break;
                case 2:
                    DeliveryGoods.DeliveryGoodsInfo();
                    break;
                case 3:
                    manageUserInfo(root);
                    break;
                case 4:
                    main(null);  // 로그아웃 처리
                    break;
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
                    break;
            }
        }
    }


	private static void manageUserInfo(boolean root) throws IOException {
        Scanner sc = new Scanner(System.in);
        if (root) {
            userManager.printAllUsers();
            System.out.println("1. 회원 정보 수정 2. 회원 삭제 3. 돌아가기");
            System.out.print("번호를 입력하세요: ");
            int choice = sc.nextInt();
            sc.nextLine(); // 버퍼 비우기
            switch (choice) {
                case 1:
                    System.out.print("수정할 회원의 ID를 입력하세요: ");
                    String userId = sc.nextLine();
                    updateUser(userId);
                    break;
                case 2:
                    System.out.print("삭제할 회원의 ID를 입력하세요: ");
                    userId = sc.nextLine();
                    userManager.deleteUser(userId);
                    break;
                case 3:
                    return;
            }
        } else {
            userManager.printCurrentUser();
            System.out.println("1. 내 정보 수정 2. 돌아가기");
            System.out.print("번호를 입력하세요: ");
            int choice = sc.nextInt();
            sc.nextLine(); // 버퍼 비우기
            if (choice == 1) {
                updateUser(userManager.getCurrentUser().getId());
            } else {
                return;
            }
        }
    }

    private static void updateUser(String userId) throws IOException {
        System.out.print("새 비밀번호: ");
        String newPassword = br.readLine();
        System.out.print("새 이름: ");
        String newName = br.readLine();
        System.out.print("새 주소: ");
        String newAddress = br.readLine();
        System.out.print("새 전화번호: ");
        String newPhoneNumber = br.readLine();
        System.out.print("새 이메일: ");
        String newEmail = br.readLine();
        System.out.print("새 잔액: ");
        int newBalance = Integer.parseInt(br.readLine());

        User updatedUser = new User(userId, newPassword, newName, newAddress, newPhoneNumber, newEmail, newBalance);
        userManager.updateUser(userId, updatedUser);
        System.out.println("사용자 정보가 업데이트 되었습니다.");
    }
}

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager {
    private static final Logger LOGGER = Logger.getLogger(UserManager.class.getName());
    private static final String CSV_FILE = "member.csv";
    private static final String TEMP_FILE = "temp.csv";
    private User currentUser;  // 현재 로그인한 사용자를 저장

    public User getCurrentUser() {
        return currentUser;
    }

    public UserManager() {
        ensureFileExists(CSV_FILE);
    }

    //파일이 존재하는지 확인하고 없으면 생성하는 메소드
    private void ensureFileExists(String fileName) {
        // 제공된 파일 이름으로 새로운 File 객체 생성
        File file = new File(fileName);
        // 파일이 존재하지 않을 경우, 파일 생성 시도
        if (!file.exists()) {
            try {
                boolean isFileCreated = file.createNewFile();
                if (isFileCreated && fileName.equals(CSV_FILE)) {
                    try (FileWriter fw = new FileWriter(file, true);
                         BufferedWriter bw = new BufferedWriter(fw)) {
                        bw.write("ID,PW,USERNAME,ADDRESS,PHONE,EMAIL,CREDIT\n");
                        //헤더 작성에 문제가 생겼을 경우
                    } catch (IOException e) {
                        LOGGER.log(Level.SEVERE, "Error writing header to " + fileName, e);
                    }
                    //파일 생성에 문제가 생겼을 경우
                } else if (!isFileCreated) {
                    LOGGER.log(Level.WARNING, "Failed to create " + fileName);
                }
                //파일 생성 중 문제가 생겼을 경우
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating " + fileName, e);
            }
        }
    }

    //회원가입 메소드
    public void signup(User user) {
        //파일이 존재하는지 확인
        ensureFileExists(CSV_FILE);
        try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
            String userData = String.format("%s,%s,%s,%s,%s,%s,%d%n",
                    user.getId(), user.getPassword(), user.getName(), user.getAddress(),
                    user.getPhoneNumber(), user.getEmail(), user.getBalance());
            writer.append(userData);
            System.out.println("회원가입이 완료되었습니다.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in signup", e);
        }
    }

    //로그인 메소드(id와 password를 매개변수로 받아와서 처리) 성공시 true 실패시 false 반환
    public boolean login(String id, String password) {
        //파일이 존재하는지 확인
        ensureFileExists(CSV_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            reader.readLine(); // csv 첫 줄은 필드라 건너뜀
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id) && parts[1].equals(password)) {
                    // 로그인 성공, 사용자 객체 생성 및 저장
                    currentUser = new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], Integer.parseInt(parts[6]));
                    return true;
                }
            }
            //로그인 하는데 문제가 생겼을 경우
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in login", e);
        }
        return false;
    }

    //회원가입시 아이디 중복 체크 메소드. 이미 존재할 경우 true 없을시 false 반환
    public boolean checkDuplicateId(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in checkDuplicateId", e);
        }
        return false;
    }

    //root 계정에서 회원정보 출력하는 메소드
    public void printAllUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            System.out.println("=== 모든 회원 정보 ===");
            reader.readLine(); //첫 줄은 필드이기 때문에 줄바꿈을 통해 출력x
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], Integer.parseInt(parts[6]));
                System.out.println(user);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in printAllUsers", e);
        }
    }

    //로그인한 회원 정보 출력
    public void printCurrentUser() {
        if (currentUser != null) {
            System.out.println("=== 현재 로그인한 사용자 정보 ===");
            System.out.println(currentUser);  // User 클래스의 toString() 메소드를 활용
        } else {
            System.out.println("로그인한 사용자가 없습니다.");
        }
    }

    // 로그인한 회원의 정보를 업데이트하는 메소드
    public void updateUser(String id, User newUser) {
        // CSV 파일과 임시 파일이 존재하는지 확인하고 없으면 생성
        ensureFileExists(CSV_FILE);
        ensureFileExists(TEMP_FILE);

        // 입력 파일과 임시 파일 객체 생성
        File inputFile = new File(CSV_FILE);
        File tempFile = new File(TEMP_FILE);

        // 파일 읽기 및 쓰기를 위한 BufferedReader와 BufferedWriter 생성
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            // 파일 끝까지 한 줄씩 읽으면서 반복
            while ((currentLine = reader.readLine()) != null) {
                String[] userData = currentLine.split(",");
                // 현재 줄의 ID가 업데이트 대상 ID와 일치하지 않으면 그대로 임시 파일에 씀
                if (!userData[0].equals(id)) {
                    writer.write(currentLine + System.lineSeparator());
                } else {
                    // 일치하는 경우 새로운 사용자 정보로 업데이트된 데이터를 생성하여 임시 파일에 씀
                    String updatedData = String.format("%s,%s,%s,%s,%s,%s,%d%n",
                            newUser.getId(), newUser.getPassword(), newUser.getName(),
                            newUser.getAddress(), newUser.getPhoneNumber(),
                            newUser.getEmail(), newUser.getBalance());
                    writer.write(updatedData);
                    if (currentUser != null && currentUser.getId().equals(id)) {
                        // 현재 로그인한 사용자의 정보를 갱신
                        currentUser = newUser;
                    }
                }
            }
        } catch (IOException e) {
            // 파일 읽기/쓰기 중 오류 발생 시 로깅 후 메소드 종료
            LOGGER.log(Level.SEVERE, "Error in updateUser", e);
            return;
        }
        if(!replaceFile(inputFile, tempFile)) {
            System.out.println("Error in updateUser");
        }
    }

        //회원 정보 삭제
        public void deleteUser(String id) {
            ensureFileExists(CSV_FILE);
            ensureFileExists(TEMP_FILE);

            File inputFile = new File(CSV_FILE);
            File tempFile = new File(TEMP_FILE);

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                String currentLine;
                // 파일 끝까지 한 줄씩 읽으면서 반복
                while ((currentLine = reader.readLine()) != null) {
                    String[] userData = currentLine.split(",");
                    if (!userData[0].equals(id)) {
                        writer.write(currentLine + System.lineSeparator());
                    }
                }
            } catch (IOException e) {
                // 파일 삭제 중 오류 발생 시 로깅 후 메소드 종료
                LOGGER.log(Level.SEVERE, "Error in deleteUser", e);
                return;
            }
            if(!replaceFile(inputFile, tempFile)) {
                System.out.println("Error deleting");
            }
        }
    // 파일 교체 메소드
    private boolean replaceFile(File oldFile, File newFile) {
        // 기존 파일을 삭제하고 실패한 경우 로깅
        if (!oldFile.delete()) {
            LOGGER.log(Level.WARNING, "원본파일을 삭제할 수 없습니다.");
            return false;
        }
        // 임시 파일을 기존 파일명으로 바꾸고 실패할 경우 로깅
        if (!newFile.renameTo(oldFile)) {
            LOGGER.log(Level.WARNING, "파일명을 바꿀 수 없습니다.");
            return false;
        }
        return true;
    }
}

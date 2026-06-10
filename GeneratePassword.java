import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        
        // 生成 123456 的 BCrypt 哈希
        String password = "123456";
        String hash = encoder.encode(password);
        
        System.out.println("明文密码: " + password);
        System.out.println("BCrypt哈希: " + hash);
        
        // 验证
        boolean matches = encoder.matches(password, hash);
        System.out.println("验证结果: " + matches);
    }
}

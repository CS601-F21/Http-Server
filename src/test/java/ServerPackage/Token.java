package ServerPackage;

/**
 * Helper class to process a json file containing
 * a token
 */
public class Token {
    private String token;

    /**
     * Constructor.
     * @param token
     */
    public Token(String token) {
        this.token = token;
    }

    /**
     * Return the token.
     * @return
     */
    public String getToken() {
        return token;
    }
}

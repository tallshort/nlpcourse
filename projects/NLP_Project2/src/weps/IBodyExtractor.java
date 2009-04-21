package weps;

public interface IBodyExtractor {
    /**
     * Extracts main description (body) from the specified document.
     * 
     * @param filePath
     *            the specified document path
     * @return the body of the specified document
     */
    public String extractBody(String filePath);
}

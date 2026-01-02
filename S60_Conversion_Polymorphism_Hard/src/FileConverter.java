import java.nio.file.Path;

/**
 * Plugin contract. External plugins must implement this interface.
 * <p>
 * Implementations should be side-effect free except for reading input and
 * writing output; avoid using System.exit in plugins.
 */
interface FileConverter {
    /**
     * Convert contents from the input file and write results to the output file.
     * Implementations are responsible for choosing formats and handling errors.
     */
    void convert(Path input, Path output) throws Exception;
}
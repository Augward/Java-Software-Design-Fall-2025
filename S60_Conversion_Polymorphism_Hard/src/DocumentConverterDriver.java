// Java

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Run-time plugin loader that:
 * - defines FileConverter interface
 * - loads a converter class from an external plugin file (.jar or .class directory root)
 * - instantiates it dynamically
 * - runs conversion: read input, convert contents, write output
 * Usage:
 * java DocumentConverterDriver <pluginPath> <pluginClass> <inputFile> <outputFile>
 * Examples:
 * # From a JAR:
 * java DocumentConverterDriver plugins/converters.jar com.example.plugins.DocBookToMarkdown input.xml output.md
 * # From compiled .class files in a directory:
 * java DocumentConverterDriver build/classes/java/main com.example.plugins.DocBookToASCII input.xml output.txt
 */
public class DocumentConverterDriver {

    public static void main(String[] args) {
        if (args.length == 1 && ("-h".equals(args[0]) || "--help".equals(args[0]))) {
            printHelpAndExit(0);
        }
        if (args.length != 4) {
            System.err.println("Invalid arguments.");
            printHelpAndExit(1);
        }

        Path pluginPath = Paths.get(args[0]);
        String pluginClassName = args[1];
        Path inputPath = Paths.get(args[2]);
        Path outputPath = Paths.get(args[3]);

        if (!Files.exists(pluginPath)) {
            System.err.println("Plugin path does not exist: " + pluginPath.toAbsolutePath());
            System.exit(2);
        }
        if (!Files.exists(inputPath)) {
            System.err.println("Input file does not exist: " + inputPath.toAbsolutePath());
            System.exit(3);
        }
        try {
            Files.createDirectories(outputPath.getParent() != null ? outputPath.getParent() : Paths.get("."));
        } catch (IOException e) {
            System.err.println("Failed to ensure output directory exists: " + e.getMessage());
            System.exit(4);
        }

        URLClassLoader loader = null;
        try {
            URL[] urls = toClassLoaderUrls(pluginPath);
            // Parent is the current classloader so FileConverter interface is shared
            loader = new URLClassLoader(urls, DumpDocBookXMLObjectModelDriver.class.getClassLoader());

            Class<?> raw = Class.forName(pluginClassName, true, loader);
            if (!FileConverter.class.isAssignableFrom(raw)) {
                System.err.println("Plugin class does not implement FileConverter: " + pluginClassName);
                System.exit(5);
            }

            @SuppressWarnings("unchecked")
            Class<? extends FileConverter> pluginClass = (Class<? extends FileConverter>) raw;

            /* Instantiate the plugin class that is specified on the command line. */
            FileConverter converter = instantiate(pluginClass);

            System.out.println("Loaded converter: " + pluginClass.getName());
            System.out.println("Converting: " + inputPath.toAbsolutePath() + " -> " + outputPath.toAbsolutePath());
            long start = System.nanoTime();
            converter.convert(inputPath, outputPath);
            long end = System.nanoTime();
            System.out.println("Conversion complete in " + ((end - start) / 1_000_000) + " ms.");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find plugin class: " + pluginClassName);
            e.printStackTrace();
            System.exit(6);
        } catch (ReflectiveOperationException e) {
            System.err.println("Failed to instantiate plugin: " + e.getMessage());
            e.printStackTrace();
            System.exit(7);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            e.printStackTrace();
            System.exit(8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(loader);
        }
    }

    private static void printHelpAndExit(int code) {
        String usage = """
                Usage:
                  java DumpDocBookXMLObjectModel <pluginPath> <pluginClass> <inputFile> <outputFile>
                
                Arguments:
                  pluginPath   Path to plugin JAR or directory root containing compiled classes.
                  pluginClass  Fully-qualified class name implementing FileConverter.
                  inputFile    Path to input file to convert.
                  outputFile   Path where the converted output will be written.
                
                Examples:
                  java DumpDocBookXML_ObjectModel plugins/converters.jar com.example.plugins.DocBookToMarkdown src/simple.xml out/simple.md
                  java DumpDocBookXML_ObjectModel build/classes/java/main com.example.plugins.DocBookToASCII src/simple.xml out/simple.txt
                """;
        System.out.println(usage);
        System.exit(code);
    }

    private static URL[] toClassLoaderUrls(Path pluginPath) throws MalformedURLException {
        if (Files.isDirectory(pluginPath)) {
            // Load classes from the directory root; ensure trailing slash
            URL dirUrl = pluginPath.toAbsolutePath().toUri().toURL();
            return new URL[]{ensureTrailingSlash(dirUrl)};
        } else {
            // JAR file
            URL jarUrl = pluginPath.toAbsolutePath().toUri().toURL();
            return new URL[]{jarUrl};
        }
    }

    // Ensure URL for directories ends with a slash (URLClassLoader requirement for proper resource resolution)
    private static URL ensureTrailingSlash(URL url) throws MalformedURLException {
        String s = url.toString();
        if (!s.endsWith("/")) {
            return URI.create(s + "/").toURL();
        }
        return url;
    }

    private static FileConverter instantiate(Class<? extends FileConverter> pluginClass) throws ReflectiveOperationException {
        // Prefer public no-arg constructor
        try {
            Constructor<? extends FileConverter> ctor = pluginClass.getDeclaredConstructor();
            if (!Modifier.isPublic(ctor.getModifiers())) {
                ctor.setAccessible(true);
            }
            return ctor.newInstance();
        } catch (NoSuchMethodException nsme) {
            // Fall back: a single constructor with no parameters required for this runner
            Constructor<?>[] ctors = pluginClass.getDeclaredConstructors();
            for (Constructor<?> c : ctors) {
                if (c.getParameterCount() == 0) {
                    if (!Modifier.isPublic(c.getModifiers())) {
                        c.setAccessible(true);
                    }
                    return (FileConverter) c.newInstance();
                }
            }
            throw new NoSuchMethodException("Plugin has no no-arg constructor: " + pluginClass.getName());
        }
    }

    private static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ignore) {
            }
        }
    }
}


import static org.junit.Assert.*;
import org.junit.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MarkdownParseTest {
  void assertResult(String testFilename, List<String> expected) {
    try {
      String contents = Files.readString(Path.of(testFilename));
      List<String> links = MarkdownParse.getLinks(contents);
      assertEquals(testFilename, expected, links);
    } catch (IOException e) {
      fail("IOException while reading '" + testFilename + "'");
    }
  }

  @Test
  public void testBackslashEscapes() {
    assertResult("test-backslash-escapes.md",
        List.of("/close_bracket", "/single_)bracket", "/double_\\",
            "/triple_\\)bracket", "/quad_\\\\", "/open_(paren"));
  }

  @Test
  public void testEmpty() {
    assertResult("test-empty.md", List.of());
  }

  @Test
  public void testEndingText() {
    assertResult("test-ending-text.md",
        List.of("https://something.com", "some-page.html"));
  }

  @Test
  public void testSimpleFile() {
    assertResult("test-file.md",
        List.of("https://something.com", "some-page.html"));
  }

  @Test
  public void testIgnoreImage() {
    assertResult("test-ignore-image.md",
        List.of("/actual_link1", "/actual_link2", "/actual_link3"));
  }

  @Test
  public void testLabReport() {
    assertResult("test-lab-report.md",
        List.of("index.md", "https://code.visualstudio.com/download",
            "https://sdacs.ucsd.edu/~icc/index.php",
            "https://sdacs.ucsd.edu/cgi-bin/alloc-query",
            "https://docs.microsoft.com/en-us/windows-server/" +
                "administration/openssh/openssh_install_firstuse"));
  }

  @Test
  public void testParenInsideBracket() {
    assertResult("test-paren-inside-bracket.md",
        List.of("https://example.com"));
  }

  @Test
  public void testParensInsideLink() {
    assertResult("test-parens-inside-link.md", List.of("link(paren)ee"));
  }

  @Test
  public void testSpaceInURL() {
    assertResult("test-space-in-url.md",
        List.of());
  }

  @Test
  public void testUnclosedBracket() {
    assertResult("test-unclosed-bracket.md",
        List.of("https://example.com", "https://example.com/2"));
  }

  @Test
  public void testUnclosedOpenParenThenLink() {
    assertResult("test-unclosed-open-paren-then-link.md",
        List.of("link2", "link3"));
  }

  @Test
  public void testUnmatchedParen() {
    assertResult("test-unclosed-paren.md",
        List.of("https://example.com", "https://example.com/2"));
  }

  @Test
  public void testUnclosedCloseParen() {
    assertResult("test-unmatched-close-paren.md",
        List.of("link3", "link6"));
  }

  @Test
  public void testLineSkip() {
    assertResult("test-line-skip.md",
        List.of("https://something.com", "some-page.html"));
  }

  @Test
  public void testFencedCodeBlock() {
    assertResult("test-fenced-code-block.md",
        List.of("yes-link"));
  }

  @Test
  public void testSnippet1() {
    assertResult("test-snippet-1.md",
        List.of("`google.com", "google.com", "ucsd.edu"));
  }

  @Test
  public void testSnippet2() {
    assertResult("test-snippet-2.md",
        List.of("a.com", "a.com(())", "example.com"));
  }

  @Test
  public void testSnippet3() {
    assertResult("test-snippet-3.md",
        List.of("https://ucsd-cse15l-w22.github.io/"));
  }
}

CLASSPATH = lib/*:.

.PHONY: test
test: MarkdownParseTest.class MarkdownParse.class
	java -cp $(CLASSPATH) org.junit.runner.JUnitCore MarkdownParseTest

MarkdownParse.class: MarkdownParse.java
	javac MarkdownParse.java

MarkdownParseTest.class: MarkdownParseTest.java MarkdownParse.class
	javac -cp $(CLASSPATH) MarkdownParseTest.java

TryCommonMark.class: TryCommonMark.java
	javac -g -cp $(CLASSPATH) TryCommonMark.java

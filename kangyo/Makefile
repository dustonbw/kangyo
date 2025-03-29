classes:
	javac src/com/dustonbw/kangyo/Kangyo.java \
	src/com/dustonbw/kangyo/Token.java \
	src/com/dustonbw/kangyo/TokenType.java \
	src/com/dustonbw/kangyo/Scanner.java \
	-d ./bin/
	
jar: classes
	cd ./bin/ && \
	jar -cvfe kangyo.jar com.dustonbw.kangyo.Kangyo com/dustonbw/kangyo/*

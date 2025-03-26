package com.dustonbw.kangyo;

import static com.dustonbw.kangyo.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Character;

public class Scanner {
	
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	
	private int start = 0;
	private int current = 0;
	private int line = 1;
	
	private static final Map<String, TokenType> keywords;
	
	static {
		keywords = new HashMap<>();
		keywords.put("や", AND);
		keywords.put("階層", CLASS);
		keywords.put("その他", ELSE);
	    keywords.put("似非", FALSE);
	    keywords.put("for", FOR);
	    keywords.put("関数", FUN);
	    keywords.put("なら", IF);
	    keywords.put("無い", NIL);
	    keywords.put("か", OR);
	    keywords.put("表示", PRINT);
	    keywords.put("返事", RETURN);
	    keywords.put("上位", SUPER);
	    keywords.put("これの", THIS);
	    keywords.put("正", TRUE);
	    keywords.put("定数", VAR);
	    keywords.put("の間", WHILE);
	}
	
	Scanner(String source) {
		this.source = source;
	}
	
	List<Token> scanTokens() {
		while (!isAtEnd()) {
			start = current;
			scanToken();
		}
		
		tokens.add(new Token(EOF, "", null, line));
		return tokens;
	}
	
	private void scanToken() {
		char c = advance();
		switch (c) {
		case '（' : addToken(LEFT_PAREN); break;
		case '）' : addToken(RIGHT_PAREN); break;
		case '｛' : addToken(LEFT_BRACE); break;
		case '｝' : addToken(RIGHT_BRACE); break;
		case '、' : addToken(COMMA); break;
		case '．' : addToken(DOT); break;
		case '-' : addToken(MINUS); break;
		case '+' : addToken(PLUS); break;
		case '；' : addToken(SEMICOLON); break;
		case '*' : addToken(STAR); break;
		case '!' :
			addToken(match('=') ? BANG_EQUAL : BANG);
			break;
		case '=' :
			addToken(match('=') ? EQUAL_EQUAL : EQUAL);
			break;
		case '<' :
			addToken(match('=') ? LESS_EQUAL : LESS);
			break;
		case '>' :
			addToken(match('=') ? GREATER_EQUAL : GREATER);
			break;
		case '/' :
			if (match('/')) { // comments
				while (peek() != '\n' && !isAtEnd()) advance();
			} else {
				addToken(SLASH);
			}
			break;
			
		// other 'meaningless' characters
		case ' ' :
		case '\r' :
		case '\t' :
		case '　': // full-width space
			break;
		
		case '\n' :
			line++;
			break;
			
		// string literals
		case '"' : string(); break;

		default:
			if (isDigit(c)) {
				number();
			} else if (isAlpha(c)) {
				identifier();
			} else {
				Kangyo.error(line,  "Unexpected character '" + c +"'");
				break;
			}
		}
	}
	
	private void identifier() {
		while (isAlphaNumeric(peek())) advance();
		
		String text = source.substring(start, current);
		TokenType type = keywords.get(text);
		if (type == null) type = IDENTIFIER;
		addToken(type);
	}
	
	private void number() {
		while (isDigit(peek())) advance();
		
		if (peek() == '.' && isDigit(peekNext())) {
			advance();
			
			while (isDigit(peek())) advance();
		}
		
		addToken(NUMBER
				, Double.parseDouble(source.substring(start, current)));
	}
	
	private void string() {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') line++;
			advance();
		}
		
		if (isAtEnd()) {
			Kangyo.error(line, "Unterminated string");
			return;
		}
		
		advance(); // encountering closing "
		
		String value = source.substring(start + 1, current - 1);
		addToken(STRING, value);
	}
	
	private boolean match(char expected) {
		if (isAtEnd()) return false;
		if (source.charAt(current) != expected) return false;
		
		current++;
		return true;
	}
	
	private char peek() {
		if (isAtEnd()) return '\0';
		return source.charAt(current);
	}
	
	private char peekNext() {
		if (current + 1 >= source.length()) return '\0';
		return source.charAt(current + 1);
	}
	
	private boolean isAlpha(char c) {
		return (Character.isAlphabetic(c))
			|| (Character.isIdeographic(c))      // kanji
			|| (c >= '\u3041' && c <= '\u309f')  // hiragana
			|| (c >= '\u30ad' && c <= '\u30ff'); // katakana
	}
	
	private boolean isAlphaNumeric(char c) {
		return isAlpha(c) || isDigit(c);
	}
	
	private boolean isDigit(char c) {
		return (Character.isDigit(c));
	}
	
	private boolean isAtEnd() {
		return current >= source.length();
	}
	
	private char advance() {
		return source.charAt(current++);
	}
	
	private void addToken(TokenType type) {
		addToken(type, null);
	}
	
	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}

}

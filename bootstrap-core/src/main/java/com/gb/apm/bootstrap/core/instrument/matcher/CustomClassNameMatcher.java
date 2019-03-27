package com.gb.apm.bootstrap.core.instrument.matcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CustomClassNameMatcher implements CustomMatcher {
	
	public final FilterMode filterMode;
	public final String filter;
	private volatile Pattern pattern;
	
	
	public static class Or implements CustomMatcher{
		private List<CustomMatcher> matchers = new ArrayList<>();
		public boolean match(String targetClassName) {
			for(CustomMatcher matcher : matchers) {
				if(matcher.match(targetClassName)) {
					return true;
				}
			}
			return false;
		}
		
		public void add(CustomMatcher matcher) {
			matchers.add(matcher);
		}
	}
	
	public static class And implements CustomMatcher{
		
		private List<CustomMatcher> matchers = new ArrayList<>();
		
		boolean containEqual = false;
		
		public boolean match(String targetClassName) {
			for(CustomMatcher matcher : matchers) {
				if(!matcher.match(targetClassName)) {
					return false;
				}
			}
			return true;
		}
		
		public void add(CustomMatcher matcher) {
			if(matcher instanceof CustomClassNameMatcher) {
				if(((CustomClassNameMatcher) matcher).filterMode == FilterMode.Equal) {
					this.containEqual = true;
				}
			}
			matchers.add(matcher);
		}
	}
	
	public static class CustomClassNameMatcherBuilder {
		
		Or groupMatcher;
		
		And tmpAnd;
		
		public CustomClassNameMatcherBuilder() {}
		
		public CustomClassNameMatcherBuilder startWith(String filter) {
			if(tmpAnd == null) {
				tmpAnd = new And();
			}else {
				if(tmpAnd.containEqual) {
					throw new IllegalStateException("equal条件不能和其他条件并存");
				}
			}
			tmpAnd.add(CustomClassNameMatcher.startWith(filter));
			return this;
		}
		
		public CustomClassNameMatcherBuilder endWith(String filter) {
			if(tmpAnd == null) {
				tmpAnd = new And();
			}else {
				if(tmpAnd.containEqual) {
					throw new IllegalStateException("equal条件不能和其他条件并存");
				}
			}
			tmpAnd.add(CustomClassNameMatcher.endWith(filter));
			return this;
		}
		
		public CustomClassNameMatcherBuilder regxWith(String filter) {
			if(tmpAnd == null) {
				tmpAnd = new And();
			}else {
				if(tmpAnd.containEqual) {
					throw new IllegalStateException("equal条件不能和其他条件并存");
				}
			}
			tmpAnd.add(CustomClassNameMatcher.regxWith(filter));
			return this;
		}
		
		public CustomClassNameMatcherBuilder containWith(String filter) {
			if(tmpAnd == null) {
				tmpAnd = new And();
			}else {
				if(tmpAnd.containEqual) {
					throw new IllegalStateException("equal条件不能和其他条件并存");
				}
			}
			tmpAnd.add(CustomClassNameMatcher.containWith(filter));
			return this;
		}
		
		public CustomClassNameMatcherBuilder equal(String filter) {
			if(tmpAnd == null) {
				tmpAnd = new And();
			}else {
				if(tmpAnd.matchers.size() > 0) {
					throw new IllegalStateException("equal条件不能和其他条件并存");
				}
			}
			tmpAnd.add(CustomClassNameMatcher.equal(filter));
			return this;
		}
		
		public CustomClassNameMatcherBuilder or() {
			if(tmpAnd == null && groupMatcher == null) {
				throw new IllegalStateException("不能以or开头");
			}
			if(tmpAnd == null && groupMatcher == null) {
				throw new IllegalStateException("不能以or结尾");
			}
			if(tmpAnd != null) {
				if(groupMatcher == null) {
					groupMatcher = new Or();
				}
				groupMatcher.add(tmpAnd);
				tmpAnd = null;
			}
			return this;
		}
		
		public CustomMatcher build() {
			if(tmpAnd != null) {
				if(groupMatcher == null) {
					groupMatcher = new Or();
				}
				groupMatcher.add(tmpAnd);
				tmpAnd = null;
			}else {
				throw new IllegalStateException("不能以or结尾");
			}
			return groupMatcher;
		}
	}
	
	public static CustomClassNameMatcher regxWith(String filter) {
		return new CustomClassNameMatcher(FilterMode.Regex,filter);
	}
	
	public static CustomClassNameMatcher endWith(String filter) {
		return new CustomClassNameMatcher(FilterMode.Last,filter);
	}
	
	public static CustomClassNameMatcher startWith(String filter) {
		return new CustomClassNameMatcher(FilterMode.Fisrt,filter);
	}
	
	public static CustomClassNameMatcher containWith(String filter) {
		return new CustomClassNameMatcher(FilterMode.Like,filter);
	}
	
	public static CustomClassNameMatcher equal(String filter) {
		return new CustomClassNameMatcher(FilterMode.Equal,filter);
	}
	
	private CustomClassNameMatcher(FilterMode filterMode,String filter) {
		this.filter = filter;
		this.filterMode = filterMode;
	}
	
	
	public boolean match(String targetClassName) {
		if (this.filterMode == FilterMode.Equal) {
			return targetClassName.equals(this.filter);
		}
		if (this.filterMode == FilterMode.Fisrt) {
			return targetClassName.indexOf(this.filter) == 0;
		}
		if (this.filterMode == FilterMode.Last) {
			return targetClassName.lastIndexOf(this.filter) == targetClassName.length() - this.filter.length();
		}
		if (this.filterMode == FilterMode.Like) {
			return targetClassName.contains(this.filter);
		}
		if (this.filterMode == FilterMode.Regex) {
			if (this.pattern == null) {
				this.pattern = Pattern.compile(this.filter);
			}
			return this.pattern.matcher(targetClassName).matches();
		}
		return false;
	}
	
	public enum FilterMode {
		/**
		 * 表示与目标内容完全匹配。
		 */
		Equal,
		/**
		 * 表示目标内容以指定的内容开头。
		 */
		Fisrt,
		/**
		 * 表示目标内容以指定的内容结尾。
		 */
		Last,
		/**
		 * 表示目标内容包含指定的内容。
		 */
		Like,
		/**
		 * 表示目标内容按指定的正则表达式进行匹配。
		 */
		Regex
	}
}

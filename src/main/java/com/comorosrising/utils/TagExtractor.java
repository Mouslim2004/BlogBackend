package com.comorosrising.utils;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TagExtractor {

    private static final Pattern TAG_PATTERN = Pattern.compile("#([a-zA-Z0-9_]+)");

    public Set<String> extractTags(String content){
        Set<String> extractedTags = new HashSet<>();
        if(content != null){
            Matcher matcher = TAG_PATTERN.matcher(content);
            while (matcher.find()){
                extractedTags.add(matcher.group(1));
            }
        }

        return extractedTags;
    }
}

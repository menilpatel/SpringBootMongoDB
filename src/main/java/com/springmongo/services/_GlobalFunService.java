package com.springmongo.services;

import org.springframework.stereotype.Service;

@Service
public class _GlobalFunService {

	public String replaceSpCharAndSpace(String input) {
        String sanitizedString = input.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Replace spaces with underscores
        sanitizedString = sanitizedString.replaceAll("\\s", "_");

        return sanitizedString;
    }
}

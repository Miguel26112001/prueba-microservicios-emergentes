package com.example.ai.agent.application.internal;

import com.example.ai.agent.infrastructure.clients.sales.requests.ProductRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProductExtractionService {

  public List<ProductRequest> extract(
      String text
  ) {

    List<ProductRequest> requests =
        new ArrayList<>();

    Pattern pattern = Pattern.compile(
        "(\\d+)\\s+([a-zA-Z0-9áéíóúñ\\-\\s]+?)(?=\\d|$|y|,)"
    );

    Matcher matcher =
        pattern.matcher(text.toLowerCase());

    while (matcher.find()) {

      requests.add(
          new ProductRequest(
              matcher.group(2).trim(),
              Integer.parseInt(
                  matcher.group(1)
              )
          )
      );
    }

    return requests;
  }
}
package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    // In-memory balance simulator
    private double balance = 1000.00;

    @GetMapping("/balance")
    public Map<String, Object> getBalance() {
        Map<String, Object> response = new HashMap<>();
        response.put("balance", balance);
        return response;
    }

    @PostMapping("/transaction")
    public Map<String, Object> handleTransaction(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String type = payload.get("type");
        double amount;

        try {
            amount = Double.parseDouble(payload.get("amount"));
        } catch (Exception e) {
            response.put("error", "Invalid amount format.");
            return response;
        }

        if (amount <= 0) {
            response.put("error", "Amount must be greater than zero.");
            return response;
        }

        if ("deposit".equalsIgnoreCase(type)) {
            balance += amount;
            response.put("message", "Successfully deposited $" + String.format("%.2f", amount));
        } else if ("withdraw".equalsIgnoreCase(type)) {
            if (amount > balance) {
                response.put("error", "Insufficient funds.");
                return response;
            }
            balance -= amount;
            response.put("message", "Successfully withdrew $" + String.format("%.2f", amount));
        } else {
            response.put("error", "Invalid transaction type.");
            return response;
        }

        response.put("balance", balance);
        return response;
    }
}
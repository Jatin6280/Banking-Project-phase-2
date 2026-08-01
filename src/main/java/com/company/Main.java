package com.company;

import java.util.Scanner;
import java.util.Random;

public class Main {

    // Helper method to generate OTP
    public static int generateOTP() {
        return 100000 + new Random().nextInt(900000);
    }

    // Moved inside class and made static
    public static boolean panCheck(String panNumber) {
        return panNumber.matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==== Digital Banking System =====");
            System.out.println("1. Register Customer");
            System.out.println("2. Login Customer");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character left by nextInt()

            if (choice == 3) break;

            if (choice == 1) {
                System.out.println("Enter Name:");
                String name = sc.nextLine();
                System.out.println("Enter email:");
                String email = sc.nextLine();

                // OTP Verification

                int generatedOtp = generateOTP();

                EmailService emailService = new EmailService();
                emailService.sendEmail(email, generatedOtp);

                System.out.println("(System debug: Your OTP is " + generatedOtp + ")");
                System.out.println("OTP method finished.");
                boolean verified = false;
                for (int limit = 0; limit < 3; limit++) {
                    System.out.print("Enter OTP: ");
                    int otp = sc.nextInt();
                    if (otp == generatedOtp) {
                        System.out.println("Email is verified!");
                        verified = true;
                        break;
                    } else {
                        System.out.println("Wrong OTP. Attempts left: " + (2 - limit));
                    }
                }

                if (!verified) {
                    System.out.println("OTP failed. Registration aborted.");
                    continue; // Go back to main menu
                }

                // PAN Verification

                String pan = "";
                boolean panVerified = false;

                for (int limit = 0; limit < 3; limit++) {
                    System.out.print("Enter your PAN Number: ");
                    pan = sc.next();

                    if (panCheck(pan)) {
                        System.out.println("PAN Verified Successfully.");
                        panVerified = true;
                        break;
                    } else {
                        System.out.println("Invalid PAN format.");
                    }
                }

                if (panVerified) {
                    System.out.println("Registration Successful for " + name);
                } else {
                    System.out.println("Registration failed due to invalid PAN.");
                }
                String password="";
                while (true) {
                    System.out.println("Welcome  to   Jatin  bank\n Create password");
                     password = sc.next();
                    System.out.println("Confirm Password");
                    String confirmPassword = sc.next();
                    if(password.equals(confirmPassword)){
                        System.out.println("Your account registered succefully");
                        break;
                    }
                    System.out.println("TRy again");
                }
                RegisterDAO dao = new RegisterDAO();
                boolean success=dao.registerUser(name, pan, email, password) ;
                if (success) {
                    System.out.println("Account registered successfully!");
                } else {
                    System.out.println("Registration failed.");
                }
            }
            //login
            if (choice ==2){
                System.out.println("Enter name");
                String name = sc.next();
                System.out.println("Enter Email");
                String email= sc.next();
                System.out.println("Enter  Password");
                String password=sc.next();


            }

        }

    }
}
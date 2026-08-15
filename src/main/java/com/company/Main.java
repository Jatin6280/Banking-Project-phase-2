package com.company;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Random;
import java.util.List;
public class Main {
    public static int accountGen() {
        return 10000000 + new SecureRandom().nextInt(90000000);
    }
    public static int generateOTP() {
        return 100000 + new Random().nextInt(900000);
    }
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
            sc.nextLine();
            if (choice == 3) {
                System.out.println("Thank you for using Jatin Bank!");
                break;
            }
            if (choice == 1) {
                System.out.println("Enter Name:");
                String name = sc.nextLine();
                System.out.println("Enter Email:");
                String email = sc.nextLine();
                int generatedOtp = generateOTP();
                EmailService emailService = new EmailService();
                emailService.sendEmail(email, generatedOtp);
                System.out.println(
                        "(System debug: Your OTP is "
                                + generatedOtp + ")"
                );
                boolean verified = false;
                for (int limit = 0; limit < 3; limit++) {
                    System.out.print("Enter OTP: ");
                    int otp = sc.nextInt();
                    if (otp == generatedOtp) {
                        System.out.println("Email is verified!");
                        verified = true;
                        break;
                    } else {
                        System.out.println(
                                "Wrong OTP. Attempts left: "
                                        + (2 - limit)
                        );
                    }
                }
                if (!verified) {
                    System.out.println(
                            "OTP failed. Registration aborted."
                    );
                    sc.nextLine();
                    continue;
                }
                sc.nextLine();
                String pan = "";
                boolean panVerified = false;
                for (int limit = 0; limit < 3; limit++) {
                    System.out.print("Enter your PAN Number: ");
                    System.out.println("this will only check formatting");
                    pan = sc.nextLine().toUpperCase();
                    if (panCheck(pan)) {
                        System.out.println(
                                "PAN Verified Successfully."
                        );
                        panVerified = true;
                        break;
                    } else {
                        System.out.println(
                                "Invalid PAN format."
                        );
                    }
                }
                if (!panVerified) {
                    System.out.println(
                            "Registration failed due to invalid PAN."
                    );
                    continue;
                }
                System.out.println(
                        "Registration Successful for " + name
                );
                String password;
                while (true) {
                    System.out.println("Welcome to Jatin Bank");
                    System.out.print("Create Password: ");
                    password = sc.nextLine();
                    System.out.print("Confirm Password: ");
                    String confirmPassword = sc.nextLine();
                    if (password.equals(confirmPassword)) {
                        System.out.println(
                                "Password confirmed."
                        );
                        break;
                    } else {
                        System.out.println(
                                "Passwords do not match. Try again.\n"
                        );
                    }
                }
                String hashedPassword =
                        PasswordUtil.hashpassword(password);
                int accountNo = accountGen();
                RegisterDAO dao = new RegisterDAO();
                boolean success = dao.registerUser(
                        name,
                        pan,
                        email,
                        hashedPassword,
                        accountNo
                );
                if (success) {
                    System.out.println(
                            "Account registered successfully!"
                    );
                    System.out.println(
                            "Your account no. " + accountNo
                    );
                } else {
                    System.out.println(
                            "Registration failed."
                    );
                }
            }
            else if (choice == 2) {
                System.out.println("Enter Name:");
                String name = sc.nextLine();
                System.out.println("Enter AccountNo.:");
                int account_no = sc.nextInt();
                System.out.println("Enter Password:");
                String password = sc.next();
                loginDAO login = new loginDAO();
                boolean success =
                        login.loginUser(account_no, password);
                if (success) {
                    System.out.println("Login successful");
                    System.out.println("Welcome " + name);
                    while (true) {
                        System.out.println(
                                "\n===== BANKING DASHBOARD ====="
                        );
                        System.out.println("1) Deposit money");
                        System.out.println("2) Withdraw");
                        System.out.println("3) Check Balance");
                        System.out.println("4) Transfer");
                        System.out.println(
                                "5) Transaction history (last 5)"
                        );
                        System.out.println("6) Logout");
                        System.out.print(
                                "Choose an option: "
                        );
                        int option = sc.nextInt();
                        sc.nextLine();
                        DepositDAO depo = new DepositDAO();
                        if (option == 6) {
                            System.out.println(
                                    "Logged out successfully."
                            );
                            break;
                        }
                        if (option == 1) {
                            System.out.println(
                                    "Enter money:"
                            );
                            BigDecimal deposit =
                                    sc.nextBigDecimal();
                            depo.deposit(
                                    account_no,
                                    deposit
                            );
                        }
                        else if (option == 2) {
                            System.out.println(
                                    "Enter withdraw Amount:"
                            );
                            BigDecimal withdraw =
                                    sc.nextBigDecimal();
                            withdrawDAO with =
                                    new withdrawDAO();
                            with.withdraw(
                                    account_no,
                                    withdraw
                            );
                        }
                        else if (option == 3) {
                            AccountDAO acc =
                                    new AccountDAO();
                            BigDecimal bal =
                                    acc.getBalance(account_no);
                            System.out.println(
                                    "Your balance is " + bal
                            );
                        }
                        else if (option == 4) {
                            transferDAO tran =
                                    new transferDAO();
                            int senderAccount =
                                    account_no;
                            System.out.println(
                                    "Write receiver account_no:"
                            );
                            int receiverAccount =
                                    sc.nextInt();
                            System.out.println(
                                    "Amount you want to transfer:"
                            );
                            BigDecimal amount =
                                    sc.nextBigDecimal();
                            tran.transfer(
                                    senderAccount,
                                    receiverAccount,
                                    amount
                            );
                        }
                        else if (option == 5) {
                            transactionDAO tran =
                                    new transactionDAO();
                            List<Transaction> transactions =
                                    tran.shortHistory(account_no);
                            if (transactions.isEmpty()) {
                                System.out.println(
                                        "No transactions found."
                                );
                            } else {
                                System.out.println(
                                        "\n===== LAST 5 TRANSACTIONS ====="
                                );
                                for (Transaction transaction :
                                        transactions) {
                                    System.out.println(
                                            "Type: "
                                                    + transaction
                                                    .getTransactionType()
                                    );
                                    System.out.println(
                                            "Amount: "
                                                    + transaction
                                                    .getAmount()
                                    );
                                    System.out.println(
                                            "Time: "
                                                    + transaction
                                                    .getTransactionTime()
                                    );
                                    System.out.println(
                                            "-------------------------"
                                    );
                                }
                            }
                        }
                        else {
                            System.out.println(
                                    "Invalid option. Please try again."
                            );
                        }
                    }
                } else {
                    System.out.println(
                            "Login failed. Try again @"
                                    + name
                    );
                }
            }
            else {
                System.out.println(
                        "Invalid option. Please choose 1, 2 or 3."
                );
            }
        }
        sc.close();
    }
}

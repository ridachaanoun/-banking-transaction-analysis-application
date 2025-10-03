"# Banking Transaction Analysis Application

## What is this?

A complete Java banking application that helps manage clients, accounts, and transactions with automatic reporting and fraud detection.

## Key Features

### Client Management
- Add, update, and delete clients
- Search clients by name or email
- View client account summaries

### Account Management
- Create Current and Savings accounts
- Track balances in real-time
- Support for overdraft and interest rates

### Transaction Processing
- Process deposits and withdrawals
- Transfer money between accounts
- Complete transaction history

### Reports & Analytics
- Monthly financial reports
- Top clients by balance
- Suspicious transaction alerts
- Inactive account detection

## How to Run

1. **Setup Database**
   - Install PostgreSQL
   - Create database named `albaraka_db`
   - Update connection details in `DBConnection.java`

2. **Compile & Run**
   ```bash
   javac -cp "lib/*" src/**/*.java
   java -cp "lib/*;src" ui.ConsoleApp
   ```

3. **Test Database Connection**
   ```bash
   java -cp "lib/*;src" resources.DatabaseConnectionTest
   ```

## Application Menu

```
=== Banking Console ===
1) Clients      - Manage customer information
2) Accounts     - Handle bank accounts
3) Transactions - Process money operations
4) Reports      - View analytics and summaries
0) Exit
```

## What You Get

✅ Complete working application  
✅ All source code  
✅ Database setup scripts  
✅ User documentation  
✅ 6 months support  

## Technical Details

- **Language**: Java 11+
- **Database**: PostgreSQL
- **Architecture**: Layered (DAO, Service, UI)
- **Security**: Input validation, fraud detection

## Why Choose This?

- **Ready to Use**: Works immediately after setup
- **Secure**: Built-in validation and monitoring
- **Scalable**: Easy to add new features
- **Professional**: Industry-standard architecture
- **Supported**: Includes maintenance and updates

## Sample Output

```
Client Dashboard:
Client: John Doe
Total Balance: $25,750.00
Accounts: 3 (2 Current, 1 Savings)
Recent Activity: $12,300.00 this month
Alerts: No suspicious activity
```

## class diagram


![App screenshot](https://github.com/ridachaanoun/-banking-transaction-analysis-application/blob/main/class%20diagram/image.png)

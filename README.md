# AdvancedOOProject

Phase 0: Database setup (day 1)
Do first – no code yet, just schema.
1.	Run the SQL script to create tables (PropertyType, PropertyStatus, TransactionType, Agent, Client, Property, Transaction).
2.	Insert initial lookup data (House, Apartment, Land; Available, Sold, Rented; Sale, Rent).
3.	Test with a simple SQL client (e.g., SQLite CLI, DBeaver) to verify.
________________________________________
Phase 1: Entity classes (POJOs) – no database, no JavaFX
Why first? They are independent and needed by every other layer.
Order	Class	Reason
1	PropertyType	Small, enum like, no external deps
2	PropertyStatus	Same
3	TransactionType	Same
4	Agent	References nothing (plain fields)
5	Client	Same
6	Property	Contains typeId, statusId, agentId (ints) – no object references yet
7	Transaction	Contains propertyId, clientId, transTypeId
Note: For simplicity, use private fields + getters/setters OR make them immutable with builders (your choice). The order doesn’t depend on that decision.
________________________________________
Phase 2: Utility classes for database
Before repositories, create a helper for JDBC connections.
Order	Class	Reason
8	DatabaseConnection	Manages Connection (SQLite or MySQL). Single static method getConnection().
________________________________________
Phase 3: Repository interfaces (abstractions)
Why before implementation? Defines the contract; allows you to later mock or swap implementations.
Order	Interface	Depends on
9	IAgentRepository	Agent
10	IClientRepository	Client
11	IPropertyRepository	Property
12	ITransactionRepository	Transaction
________________________________________
Phase 4: Concrete repositories (JDBC)
Implement one at a time, test immediately with a simple main().
Order	Class	Implements	Notes
13	AgentRepository	IAgentRepository	Start with insert, findById, findAll. Then add update, delete, findByEmailAndPassword.
14	ClientRepository	IClientRepository	Same pattern.
15	PropertyRepository	IPropertyRepository	Insert requires looking up type_id, status_id via sub queries or separate selects.
16	TransactionRepository	ITransactionRepository	Simplest – just insert and select by property/client.
Testing tip: After each repository, write a small main() in the same class (or a separate test class) that inserts, reads, updates, and deletes. No UI yet.
________________________________________
Phase 5: Service layer (business logic)
Why after repositories? Services orchestrate repositories. Start with one core service.
Order	Class	Depends on	Methods to implement first
17	PropertyService	IPropertyRepository, IPropertyStatusRepository (if created)	getAllAvailableProperties(), changePropertyStatus()
18	AgentService	IAgentRepository	authenticate(String email, String password)
19	TransactionService	ITransactionRepository, IPropertyRepository	recordSale(), getTransactionsForProperty()
You may not need all services initially – start with PropertyService because it contains the most interesting logic.
________________________________________
Phase 6: JavaFX UI (controllers and views)
Do last – now your backend is ready and tested.
Chronological order within JavaFX:
Order	Class / File	Role
20	RealEstateApp (main)	Launches the app, loads login.fxml
21	login.fxml + LoginController	First screen: email + password → calls AgentService.authenticate()
22	dashboard.fxml + DashboardController	Shows agent info, buttons to manage properties/clients
23	property-list.fxml + PropertyListController	TableView of properties, uses PropertyService
24	property-form.fxml + PropertyFormController	Add/edit property, uses PropertyRepository or service
25	client-list.fxml + ClientListController	Similar for clients
26	transaction-history.fxml + TransactionController	Shows transactions
You can build one screen at a time, wire it to the already tested repositories/services.
________________________________________
Summary table – recommended chronological order
Phase	Step	Item to implement
0	1	SQL schema + initial data
1	2–8	Entity classes (PropertyType, PropertyStatus, TransactionType, Agent, Client, Property, Transaction)
2	9	DatabaseConnection utility
3	10–13	Repository interfaces (IAgent, IClient, IProperty, ITransaction)
4	14–17	Concrete repositories (Agent, Client, Property, Transaction) – test each
5	18–20	Services (PropertyService, AgentService, TransactionService) – test each
6	21–26	JavaFX main, FXML files, controllers (one screen at a time)
________________________________________
Pro tips for practicality
•	Don’t implement everything at once. After each repository, write a tiny main() to verify CRUD.
•	Use SQLite – no server setup, just a file.
•	Hardcode the database schema creation in a separate script – don’t rely on Java to create tables.
•	For the first UI screen (login), you only need AgentRepository.findByEmailAndPassword() – implement that method early.
•	Delay complex features (e.g., image upload, search filters) until after core CRUD works.


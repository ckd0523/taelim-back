# INSERT INTO member (email, password, u_Name, role) VALUES
#                                                       ('user1@example.com', 'password1', 'User Name 1', 'USER'),
#                                                       ('user2@example.com', 'password2', 'User Name 2', 'ASSET_MANAGER'),
#                                                       ('user3@example.com', 'password3', 'User Name 3', 'USER'),
#                                                       ('user4@example.com', 'password4', 'User Name 4', 'ASSET_MANAGER'),
#                                                       ('user5@example.com', 'password5', 'User Name 5', 'USER'),
#                                                       ('user6@example.com', 'password6', 'User Name 6', 'ASSET_MANAGER'),
#                                                       ('user7@example.com', 'password7', 'User Name 7', 'USER'),
#                                                       ('user8@example.com', 'password8', 'User Name 8', 'ASSET_MANAGER'),
#                                                       ('user9@example.com', 'password9', 'User Name 9', 'USER'),
#                                                       ('user10@example.com', 'password10', 'User Name 10', 'ADMIN');
# INSERT INTO common_asset (
#     asset_classification, asset_basis, asset_code, asset_Name, purpose, quantity,
#     department, asset_Location, asset_User, asset_Owner, asset_Security_Manager,
#     operation_Status, introduced_Date, confidentiality, integrity, availability,
#     note, manufacturing_Company, ownership, purchase_Cost, purchase_Date, useful_Life,
#     depreciation_Method, warranty_Details, attachment, purchase_Source, contact_Information,
#     QRInformation, disposal_Status, request_Status, approval, demand_Check, create_Date,
#     use_State, acquisition_Route, maintenance_Period
# ) VALUES
#       ('SOFTWARE', 'COMMON', 'ASSET001', 'Laptop', 'Office Work', 10,
#        'IT_DEPARTMENT', 'MAIN_1F', 'user7@example.com', 'user7@example.com', 'user7@example.com',
#        'OPERATING', '2023-01-01', 3, 3, 2,
#        'Dell XPS 15', 'Dell', 'OWNED', 2000000, '2023-01-01', 5,
#        'FIXED_AMOUNT', '2 years warranty', NULL, 'Dell Inc.', '1234-5678',
#        'http://qrcode.com/asset001', FALSE, FALSE, 'APPROVE', TRUE, '2023-01-01',
#        'IN_USE', 'Direct Purchase', '2028-01-01'
#       ),
#       ('SOFTWARE', 'COMMON', 'ASSET002', 'MS Office', 'Office Software', 50,
#        'IT_DEPARTMENT', 'MAIN_1F', 'user7@example.com', 'user7@example.com', 'user7@example.com',
#        'OPERATING', '2023-02-01', 3, 3, 3,
#        'Microsoft Office 365', 'Microsoft', 'LEASED', 5000000, '2023-02-01', 3,
#        'FIXED_AMOUNT', '1 year subscription', NULL, 'Microsoft Inc.', '8765-4321',
#        'http://qrcode.com/asset002', FALSE, FALSE, 'APPROVE', TRUE, '2023-02-01',
#        'IN_USE', 'Annual Subscription', '2024-02-01'
#       ),
#       ('SOFTWARE', 'COMMON', 'ASSET003', 'Router', 'Network Equipment', 5,
#        'IT_DEPARTMENT', 'MAIN_1F', 'user7@example.com', 'user7@example.com', 'user7@example.com',
#        'OPERATING', '2022-11-01', 2, 2, 2,
#        'Cisco Router', 'Cisco', 'OWNED', 3000000, '2022-11-01', 7,
#        'FIXED_AMOUNT', '3 years warranty', NULL, 'Cisco Inc.', '1122-3344',
#        'http://qrcode.com/asset003', FALSE, FALSE, 'APPROVE', FALSE, '2022-11-01',
#        'IN_USE', 'Direct Purchase', '2029-11-01'
#       );
# INSERT INTO software (
#     asset_No, IP, server_Id, server_Password, company_Manager, OS
# ) VALUES
#       (1, '192.168.1.10', 'server01', 'pass01', 'John Doe', 'Windows Server 2019'),
#       (2, '192.168.1.11', 'server02', 'pass02', 'Jane Smith', 'Linux Ubuntu 20.04'),
#       (3, '192.168.1.12', 'server03', 'pass03', 'Alice Johnson', 'Windows Server 2022');

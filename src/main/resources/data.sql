/*
INSERT INTO common_asset (
    asset_classification, asset_basis, asset_code, asset_Name, purpose, quantity,
    department, asset_Location, asset_User, asset_Owner, asset_Security_Manager,
    operation_Status, introduced_Date, confidentiality, integrity, availability,
    note, manufacturing_Company, ownership, purchase_Cost, purchase_Date, useful_Life,
    depreciation_Method, warranty_Details, attachment, purchase_Source, contact_Information,
    QRInformation, disposal_Status, request_Status, approval, demand_Check, create_Date,
    use_State, acquisition_Route, maintenance_Period
) VALUES
      ('SOFTWARE', 'COMMON', 'ASSET001', 'Laptop', 'Office Work', 10,
       'IT_DEPARTMENT', 'MAIN_1F', 'admin@example.com', 'admin@example.com', 'admin@example.com',
       'OPERATING', '2023-01-01', 3, 3, 2,
       'Dell XPS 15', 'Dell', 'OWNED', 2000000, '2023-01-01', 5,
       'FIXED_AMOUNT', '2 years warranty', NULL, 'Dell Inc.', '1234-5678',
       'http://qrcode.com/asset001', FALSE, FALSE, 'APPROVE', TRUE, '2023-01-01',
       'IN_USE', 'Direct Purchase', '2028-01-01'
      ),
      ('SOFTWARE', 'COMMON', 'ASSET002', 'MS Office', 'Office Software', 50,
       'IT_DEPARTMENT', 'MAIN_1F', 'user1@example.com', 'user1@example.com', 'user1@example.com',
       'OPERATING', '2023-02-01', 3, 3, 3,
       'Microsoft Office 365', 'Microsoft', 'LEASED', 5000000, '2023-02-01', 3,
       'FIXED_AMOUNT', '1 year subscription', NULL, 'Microsoft Inc.', '8765-4321',
       'http://qrcode.com/asset002', FALSE, FALSE, 'APPROVE', TRUE, '2023-02-01',
       'IN_USE', 'Annual Subscription', '2024-02-01'
      ),
      ('SOFTWARE', 'COMMON', 'ASSET003', 'Router', 'Network Equipment', 5,
       'IT_DEPARTMENT', 'MAIN_1F', 'user2@example.com', 'user2@example.com', 'user2@example.com',
       'OPERATING', '2022-11-01', 2, 2, 2,
       'Cisco Router', 'Cisco', 'OWNED', 3000000, '2022-11-01', 7,
       'FIXED_AMOUNT', '3 years warranty', NULL, 'Cisco Inc.', '1122-3344',
       'http://qrcode.com/asset003', FALSE, FALSE, 'APPROVE', FALSE, '2022-11-01',
       'IN_USE', 'Direct Purchase', '2029-11-01'
      );
 */
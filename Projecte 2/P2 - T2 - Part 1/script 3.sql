INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_001', 'Arnau', '653877582', '98739643G');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_002', 'Anna', '627741856', '38785710U');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_003', 'Mercè', '644512288', '25133660J');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_004', 'Pere', '669660982', '35664987Q');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_005', 'Sergi', '618774020', '14421444L');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_006', 'Arnau', '677385616', '05400109Y');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_007', 'Anna', '601207040', '05441014V');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_008', 'Arnau', '666935580', '43937339U');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_009', 'Pere', '673881697', '19088657O');
INSERT INTO Client (client_id, nom, telefon, NIF) VALUES ('client_010', 'Arnau', '669424373', '54853291V');

INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_001', 'C14XZL3', 'Abarth 595 ABARTH 595 1.4 16v T-JET 107 KW (145 CV) E6D', 8931, 'client_005');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_002', '63RL8DY', 'Abarth 595 ABARTH 595 COMPETIZIONE 1.4 16v T-JET 132 KW (180 CV) E6D', 111521, 'client_007');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_003', 'PO3LJCO', 'Abarth 595 ABARTH 595 ESSEESSE 1.4 16v T-JET 132 KW (180 CV) E6D', 154081, 'client_005');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_004', 'X438B6H', 'Abarth 595 ABARTH 595 MONSTER ENERGY YAMAHA 1.4 16v T-JET 121 kW (165 CV) E6D', 134912, 'client_006');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_005', 'D4MG005', 'Abarth 595 ABARTH 595 PISTA 1.4 16v T-JET 121 kW (165 CV) E6D', 45719, 'client_008');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_006', 'PTMT19J', 'Abarth 595 ABARTH 595 SCORPIONEORO 1.4 16v T-JET 121 kW (165 CV) E6D', 133901, 'client_002');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_007', 'RB26ML1', 'Abarth 595 ABARTH 595 TURISMO 1.4 16v T-JET 121 kW (165 CV) E6D', 53701, 'client_005');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_008', '6G6QNL6', 'Abarth 595 ABARTH 595C 1.4 16v T-JET 107 KW (145 CV) E6D', 133507, 'client_006');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_009', 'NFP7BV4', 'Abarth 595 ABARTH 595C COMPETIZIONE 1.4 16v T-JET 132 KW (180 CV) E6D', 23639, 'client_005');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_010', 'Q3P00VQ', 'Abarth 595 ABARTH 595C ESSEESSE 1.4 16v T-JET 132 KW (180 CV) E6D', 188391, 'client_003');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_011', 'VOSGCW2', 'Abarth 595 ABARTH 595C MONSTER ENERGY YAMAHA 1.4 16v T-JET 121 kW (165 CV) E6D', 116290, 'client_003');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_012', 'PZX9AXC', 'Abarth 595 ABARTH 595C PISTA 1.4 16v T-JET 121 kW (165 CV) E6D', 101781, 'client_008');
INSERT INTO Vehicle (vehicle_id, num_llicencia, marca_model, kilometers, client_id) VALUES ('vehicle_013', 'F0KPGUA', 'Abarth 595 ABARTH 595C TURISMO 1.4 16v T-JET 121 KW (165 CV) E6D', 77351, 'client_007');

INSERT INTO Mecanic (mechanic_id, name, especialitat) VALUES ('mechanic_001', 'Jordi', 'Electroniques');
INSERT INTO Mecanic (mechanic_id, name, especialitat) VALUES ('mechanic_002', 'Mercè', 'Feines Pintura');
INSERT INTO Mecanic (mechanic_id, name, especialitat) VALUES ('mechanic_003', 'Jordi', 'Chapa');
INSERT INTO Mecanic (mechanic_id, name, especialitat) VALUES ('mechanic_004', 'Jordi', 'Feines Pintura');
INSERT INTO Mecanic (mechanic_id, name, especialitat) VALUES ('mechanic_005', 'Oriol', 'Feines Pintura');

INSERT INTO Reparacio (reparacio_id, descripcio, vehicle_id, data_entrada, status_reparacio, hores_dedicades) VALUES ('repair_001', 'Reparacio 001', 'vehicle_001', '2023-12-20', 'Tancada', 70);
INSERT INTO Reparacio (reparacio_id, descripcio, vehicle_id, data_entrada, status_reparacio, hores_dedicades) VALUES ('repair_002', 'Reparacio 002', 'vehicle_002', '2023-12-31', 'Tancada', 80);
INSERT INTO Reparacio (reparacio_id, descripcio, vehicle_id, data_entrada, status_reparacio, hores_dedicades) VALUES ('repair_003', 'Reparacio 003', 'vehicle_003', '2024-02-04', 'Oberta', 70);
INSERT INTO Reparacio (reparacio_id, descripcio, vehicle_id, data_entrada, status_reparacio, hores_dedicades) VALUES ('repair_004', 'Reparacio 004', 'vehicle_004', '2024-01-23', 'Oberta', 30);

INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_001_1', 'repair_001', 'mechanic_002');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_001_2', 'repair_001', 'mechanic_002');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_002_1', 'repair_002', 'mechanic_002');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_002_2', 'repair_002', 'mechanic_005');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_003_1', 'repair_003', 'mechanic_004');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_003_2', 'repair_003', 'mechanic_004');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_003_3', 'repair_003', 'mechanic_003');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_004_1', 'repair_004', 'mechanic_004');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_004_2', 'repair_004', 'mechanic_005');
INSERT INTO Reparacio_Mecanic (reparacio_mecanic_id, reparacio_id, mechanic_id) VALUES ('repair_mech_004_3', 'repair_004', 'mechanic_005');

CREATE TABLE Client
(
  client_id VARCHAR(25) PRIMARY KEY,
  nom VARCHAR(25),
  telefon VARCHAR(9),
  NIF VARCHAR(9)
);

CREATE TABLE Vehicle
(
  vehicle_id VARCHAR(25) PRIMARY KEY,
  num_llicencia VARCHAR(7),
  marca_model VARCHAR(255),
  kilometers INT,
  client_id VARCHAR(25),
  FOREIGN KEY (client_id) REFERENCES Client(client_id)
);

CREATE TABLE Mecanic
(
  mechanic_id VARCHAR(25) PRIMARY KEY,
  name VARCHAR(25),
  especialitat VARCHAR(25)
);

CREATE TABLE Reparacio
(
  reparacio_id VARCHAR(25) PRIMARY KEY,
  descripcio VARCHAR(255),
  vehicle_id VARCHAR(25),
  data_entrada DATE,
  status_reparacio VARCHAR(25),
  hores_dedicades INT,
  FOREIGN KEY (vehicle_id) REFERENCES Vehicle(vehicle_id)
);

CREATE TABLE Reparacio_Mecanic
(
  reparacio_mecanic_id VARCHAR(25) PRIMARY KEY,
  reparacio_id VARCHAR(25),
  mechanic_id VARCHAR(25),
  FOREIGN KEY (reparacio_id) REFERENCES Reparacio(reparacio_id),
  FOREIGN KEY (mechanic_id) REFERENCES Mecanic(mechanic_id)
);

CREATE TABLE Linies
(
  linies_id VARCHAR(25) PRIMARY KEY,
  reparacio_id VARCHAR(25),
  tipus_linia VARCHAR(50),
  FOREIGN KEY (reparacio_id) REFERENCES Reparacio(reparacio_id)
);

CREATE TABLE Linies_Feines_Realitzades
(
  linies_id VARCHAR(25),
  hores INT,
  PRIMARY KEY (linies_id),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

CREATE TABLE Linies_Peces_Recanvi
(
  linies_id VARCHAR(25),
  descripcio_peca VARCHAR(255),
  codi_fabricant VARCHAR(50),
  preu_unitari DECIMAL(10,2),
  unitats_usades INT,
  PRIMARY KEY (linies_id),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

CREATE TABLE Linies_Packs
(
  linies_id VARCHAR(25),
  descripcio_pack VARCHAR(255),
  preu_fix DECIMAL(10,2),
  PRIMARY KEY (linies_id),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

CREATE TABLE Altres_Conceptes
(
  linies_id VARCHAR(25),
  descripcio_concepte VARCHAR(255),
  preu DECIMAL(10,2),
  PRIMARY KEY (linies_id),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

CREATE TABLE Factura
(
  factura_id VARCHAR(25) PRIMARY KEY,
  reparacio_id VARCHAR(25),
  data_generacio DATE,
  num_factura VARCHAR(50),
  base_imposable DECIMAL(10,2),
  tipus_IVA VARCHAR(25),
  cost_ma_obra DECIMAL(10,2),
  total_amb_IVA DECIMAL(10,2),
  estat_pagament VARCHAR(25),
  FOREIGN KEY (reparacio_id) REFERENCES Reparacio(reparacio_id)
);

CREATE TABLE Descomptes
(
  descompte_id VARCHAR(25) PRIMARY KEY,
  linies_id VARCHAR(25),
  descripcio VARCHAR(255),
  quantitat_descompte DECIMAL(10,2),
  FOREIGN KEY (linies_id) REFERENCES Linies(linies_id)
);

--------------------------------------------------------------------------------

ALTER TABLE Client
ADD CONSTRAINT chk_client_telefon CHECK (LENGTH(telefon) = 9),
ADD CONSTRAINT chk_client_nif CHECK (LENGTH(NIF) = 9);

ALTER TABLE Vehicle
ADD CONSTRAINT chk_vehicle_num_llicencia CHECK (LENGTH(num_llicencia) <= 7);

ALTER TABLE Factura
ADD CONSTRAINT chk_factura_base_imposable CHECK (base_imposable >= 0),
ADD CONSTRAINT chk_factura_cost_ma_obra CHECK (cost_ma_obra >= 0),
ADD CONSTRAINT chk_factura_total_amb_IVA CHECK (total_amb_IVA >= 0);

/*CREATE TRIGGER prevent_delete_reparacio
BEFORE DELETE ON Reparacio FOR EACH ROW
BEGIN
    DECLARE cnt INT;
    SELECT COUNT(*) INTO cnt FROM Factura WHERE reparacio_id = OLD.reparacio_id;
    IF cnt > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No es pot eliminar una reparació amb una factura associada';
    END IF;
END;*/

--------------------------------------------------------------------------------

CREATE INDEX idx_client_nif ON Client(NIF);
CREATE INDEX idx_vehicle_client_id ON Vehicle(client_id);
CREATE INDEX idx_reparacio_vehicle_id ON Reparacio(vehicle_id);
CREATE INDEX idx_reparacio_mecanic_reparacio_id ON Reparacio_Mecanic(reparacio_id);
CREATE INDEX idx_reparacio_mecanic_mechanic_id ON Reparacio_Mecanic(mechanic_id);
CREATE INDEX idx_linies_reparacio_id ON Linies(reparacio_id);
CREATE INDEX idx_factura_reparacio_id ON Factura(reparacio_id);
CREATE INDEX idx_descomptes_linies_id ON Descomptes(linies_id);
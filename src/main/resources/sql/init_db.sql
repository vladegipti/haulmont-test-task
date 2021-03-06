CREATE TABLE IF NOT EXISTS patient (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(35) NOT NULL,
    patronymic VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS doctor (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(35) NOT NULL,
    patronymic VARCHAR(20) NOT NULL,
    specialization VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS recipe (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    patient_id BIGINT FOREIGN KEY REFERENCES patient(id),
    doctor_id BIGINT FOREIGN KEY REFERENCES doctor(id),
    date_of_issue DATE NOT NULL,
    valid_for INT NOT NULL,
    priority VARCHAR(11) NOT NULL
);

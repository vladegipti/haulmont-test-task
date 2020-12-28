package com.haulmont.testtask.dao;

import com.haulmont.testtask.util.DBManager;
import com.haulmont.testtask.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PatientDAO implements GenericDAO<Patient> {
    @Override
    public Patient selectOne(long id) throws SQLException {
        String query = "SELECT * FROM patient WHERE id = ?";
        Patient p = null;

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, String.valueOf(id));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p = new Patient(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                }
            }
        }
        return p;
    }

    @Override
    public List<Patient> selectAll() throws SQLException {
        String query = "SELECT * FROM patient";
        Patient p;
        List<Patient> patients = new LinkedList<>();

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                p = new Patient(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                patients.add(p);
            }
        }
        return patients;
    }

    @Override
    public void insert(Patient p) throws SQLException {
        String query = "INSERT INTO patient (first_name, last_name, patronymic, phone_number) VALUES (?, ?, ?, ?)";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setString(3, p.getPatronymic());
            ps.setString(4, p.getPhoneNumber());

            ps.execute();
        }
    }

    @Override
    public void update(Patient p) throws SQLException {
        String query = "UPDATE patient SET first_name = ?, last_name = ?, patronymic = ?, phone_number = ? WHERE id = ?";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, p.getFirstName());
            ps.setString(2, p.getLastName());
            ps.setString(3, p.getPatronymic());
            ps.setString(4, p.getPhoneNumber());
            ps.setLong(5, p.getId());

            ps.execute();
        }
    }

    @Override
    public void delete(Patient p) throws SQLException {
        String query = "DELETE FROM patient WHERE id = ?";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setLong(1, p.getId());

            ps.execute();
        }
    }
}

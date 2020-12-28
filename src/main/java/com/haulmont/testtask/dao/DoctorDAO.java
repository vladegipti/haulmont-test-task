package com.haulmont.testtask.dao;

import com.haulmont.testtask.model.Doctor;
import com.haulmont.testtask.util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DoctorDAO implements GenericDAO<Doctor> {
    @Override
    public Doctor selectOne(long id) throws SQLException {
        String query = "SELECT * FROM doctor WHERE id = ?";
        Doctor d = null;

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, String.valueOf(id));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    d = new Doctor(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                }
            }
        }
        return d;
    }

    @Override
    public List<Doctor> selectAll() throws SQLException {
        String query = "SELECT * FROM doctor";
        Doctor d;
        List<Doctor> doctors = new LinkedList<>();

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                d = new Doctor(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
                doctors.add(d);
            }
        }
        return doctors;
    }

    @Override
    public void insert(Doctor d) throws SQLException {
        String query = "INSERT INTO doctor (first_name, last_name, patronymic, specialization) VALUES (?, ?, ?, ?)";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, d.getFirstName());
            ps.setString(2, d.getLastName());
            ps.setString(3, d.getPatronymic());
            ps.setString(4, d.getSpecialization());

            ps.execute();
        }
    }

    @Override
    public void update(Doctor d) throws SQLException {
        String query = "UPDATE doctor SET first_name = ?, last_name = ?, patronymic = ?, specialization = ? WHERE id = ?";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, d.getFirstName());
            ps.setString(2, d.getLastName());
            ps.setString(3, d.getPatronymic());
            ps.setString(4, d.getSpecialization());
            ps.setLong(5, d.getId());

            ps.execute();
        }
    }

    @Override
    public void delete(Doctor d) throws SQLException {
        String query = "DELETE FROM doctor WHERE id = ?";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setLong(1, d.getId());

            ps.execute();
        }
    }
}

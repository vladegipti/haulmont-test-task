package com.haulmont.testtask.dao;

import com.haulmont.testtask.util.DBManager;
import com.haulmont.testtask.model.Doctor;
import com.haulmont.testtask.model.Patient;
import com.haulmont.testtask.model.Recipe;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RecipeDAO implements GenericDAO<Recipe> {
    GenericDAO<Doctor> doctorDAO = new DoctorDAO();
    GenericDAO<Patient> patientDAO = new PatientDAO();

    @Override
    public Recipe selectOne(long id) throws SQLException {
        String query = "SELECT * FROM recipe WHERE id = ?";
        Recipe r = null;

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, String.valueOf(id));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    r = new Recipe(rs.getLong(1), rs.getString(2), patientDAO.selectOne(rs.getLong(3)), doctorDAO.selectOne(rs.getLong(4)), rs.getDate(5), rs.getInt(6), rs.getString(7));
                }
            }
        }
        return r;
    }

    @Override
    public List<Recipe> selectAll() throws SQLException {
        String query = "SELECT * FROM recipe";
        Recipe r;
        List<Recipe> recipes = new LinkedList<>();

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                r = new Recipe(rs.getLong(1), rs.getString(2), patientDAO.selectOne(rs.getLong(3)), doctorDAO.selectOne(rs.getLong(4)), rs.getDate(5), rs.getInt(6), rs.getString(7));
                recipes.add(r);
            }
        }
        return recipes;
    }

    @Override
    public void insert(Recipe r) throws SQLException {
        String query = "INSERT INTO recipe (description, patient_id, doctor_id, date_of_issue, valid_for, priority) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, r.getDescription());
            ps.setLong(2, r.getPatient().getId());
            ps.setLong(3, r.getDoctor().getId());
            ps.setDate(4, new Date(r.getDateOfIssue().getTime()));
            ps.setInt(5, r.getValidFor());
            ps.setString(6, r.getPriority());

            ps.execute();
        }
    }

    @Override
    public void update(Recipe r) throws SQLException {
        String query = "UPDATE recipe SET description = ?, patient_id = ?, doctor_id = ?, date_of_issue = ?, valid_for = ?, priority = ? WHERE id = ?";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, r.getDescription());
            ps.setLong(2, r.getPatient().getId());
            ps.setLong(3, r.getDoctor().getId());
            ps.setDate(4, new Date(r.getDateOfIssue().getTime()));
            ps.setInt(5, r.getValidFor());
            ps.setString(6, r.getPriority());
            ps.setLong(7, r.getId());

            ps.execute();
        }
    }

    @Override
    public void delete(Recipe r) throws SQLException {
        String query = "DELETE FROM recipe WHERE id = ?";

        try (Connection con = DBManager.getDataSource().getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

            ps.setLong(1, r.getId());

            ps.execute();
        }
    }
}

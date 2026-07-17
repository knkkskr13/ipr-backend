-- ====================================================================
-- IPR MANAGEMENT SYSTEM - DATA SEED SCRIPT
-- Database: PostgreSQL
-- ====================================================================

-- Clear existing data (Order of deletion avoids foreign key violations)
-- IMPORTANT: This is intentional for test/seed data. Using TRUNCATE for efficiency.
TRUNCATE TABLE ipr_workflow_log CASCADE;
TRUNCATE TABLE ipr_declaration CASCADE;
TRUNCATE TABLE property CASCADE;
TRUNCATE TABLE ipr_return CASCADE;
TRUNCATE TABLE ipr_notification CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE employee CASCADE;
TRUNCATE TABLE office CASCADE;
TRUNCATE TABLE department CASCADE;

-- Restart all ID sequences
ALTER SEQUENCE department_sequence RESTART WITH 1;
ALTER SEQUENCE office_sequence RESTART WITH 1;
ALTER SEQUENCE employee_sequence RESTART WITH 1;
ALTER SEQUENCE user_sequence RESTART WITH 1;
ALTER SEQUENCE ipr_return_sequence RESTART WITH 1;
ALTER SEQUENCE property_sequence RESTART WITH 1;
ALTER SEQUENCE ipr_declaration_sequence RESTART WITH 1;
ALTER SEQUENCE ipr_notification_sequence RESTART WITH 1;
ALTER SEQUENCE workflow_log_sequence RESTART WITH 1;

-- 1. Seed Departments
-- HOD is only one per department (already checked in code)
INSERT INTO department (id, name) VALUES (nextval('department_sequence'), 'Finance Department'); --dept id:1
INSERT INTO department (id, name) VALUES (nextval('department_sequence'), 'Information Technology Department');--dept id:2

-- 2. Seed Offices
-- Under Finance Department (id = 1)
INSERT INTO office (id, name, department_id) VALUES (nextval('office_sequence'), 'Accounts Branch Agartala', 1);--office id:1
INSERT INTO office (id, name, department_id) VALUES (nextval('office_sequence'), 'Treasury Office Udaipur', 1);--office id:2
-- Under IT Department (id = 2)
INSERT INTO office (id, name, department_id) VALUES (nextval('office_sequence'), 'NIC State HQ Agartala', 2);--office id:3
INSERT INTO office (id, name, department_id) VALUES (nextval('office_sequence'), 'NIC IT Hub', 2);--office id:4

-- 3. Seed Employees
-- HOD of Finance Department (Dept id:21 (Office ID = 1)
INSERT INTO employee (id, name, email, service, length_of_service, present_post_held, place_of_posting, office_id)
VALUES (
    nextval('employee_sequence'), 
    'Priyanka Majumdar', 
    'priyanka.m@tripura.gov.in', 
    'Tripura Civil Service', 
    '15 years', 
    'Deputy Director Finance', 
    'Agartala', 
    1
);

-- Regular Employee under Finance Department (Dept id:1) (Office ID = 1)
INSERT INTO employee (id, name, email, service, length_of_service, present_post_held, place_of_posting, office_id)
VALUES (
    nextval('employee_sequence'), 
    'Rajan Kumar Das', 
    'rajan.das@tripura.gov.in', 
    'Tripura Civil Service', 
    '8 years', 
    'Assistant Accounts Officer', 
    'Agartala', 
    1
);

-- Regular Employee under Finance Department (Dept id:1) (Office ID = 2)
INSERT INTO employee (id, name, email, service, length_of_service, present_post_held, place_of_posting, office_id)
VALUES (
           nextval('employee_sequence'),
           'Rakes Das',
           'rakeshdas@tripura.gov.in',
           'Tripura Finance Service',
           '5 years',
           'Assistant Finance Officer',
           'GorkhaBasti',
           2
       );

-- HOD of IT Department (Dept id:2) (Office ID = 3)
INSERT INTO employee (id, name, email, service, length_of_service, present_post_held, place_of_posting, office_id)
VALUES (
    nextval('employee_sequence'), 
    'Animesh Roy', 
    'animesh.r@tripura.gov.in', 
    'Tripura IT Service', 
    '18 years', 
    'Senior Director IT', 
    'Agartala', 
    3
);

-- Regular Employee under IT Department (Dept id:2) (Office ID = 3)
INSERT INTO employee (id, name, email, service, length_of_service, present_post_held, place_of_posting, office_id)
VALUES (
    nextval('employee_sequence'), 
    'Sumit Debbarma', 
    'sumit.d@tripura.gov.in', 
    'Tripura IT Service', 
    '4 years', 
    'Scientific Assistant', 
    'Agartala', 
    3
);

-- Regular Employee under IT Department (Dept id:2) (Office ID = 4)
INSERT INTO employee (id, name, email, service, length_of_service, present_post_held, place_of_posting, office_id)
VALUES (
           nextval('employee_sequence'),
           'Soumen Debnath',
           'soumen@tripura.gov.in',
           'Tripura IT HUB',
           '7 years',
           'Assistant Officer',
           'Agartala',
           4
       );

-- 4. Seed User Credentials
-- HOD of Finance Department (employee_id = 1)
INSERT INTO users (id, username, password, role, employee_id)
VALUES (nextval('user_sequence'), 'hod_priyanka_finance', 'hod@123', 'ROLE_HOD', 1);

-- Regular Employee in Finance Department (employee_id = 2)
INSERT INTO users (id, username, password, role, employee_id)
VALUES (nextval('user_sequence'), 'emp_rajan', 'emp@123', 'ROLE_EMPLOYEE', 2);

-- Regular Employee in Finance Department (employee_id = 3)
INSERT INTO users (id, username, password, role, employee_id)
VALUES (nextval('user_sequence'), 'emp_rakesh', 'emp@123', 'ROLE_EMPLOYEE', 3);

-- HOD of IT Department (employee_id = 4)
INSERT INTO users (id, username, password, role, employee_id)
VALUES (nextval('user_sequence'), 'hod_animesh_it', 'hod@123', 'ROLE_HOD', 4);

-- Regular Employee in IT Department (employee_id = 5)
INSERT INTO users (id, username, password, role, employee_id)
VALUES (nextval('user_sequence'), 'emp_sumit', 'emp@123', 'ROLE_EMPLOYEE', 5);

-- Regular Employee in IT Department (employee_id = 6)
INSERT INTO users (id, username, password, role, employee_id)
VALUES (nextval('user_sequence'), 'emp_soumen', 'emp@123', 'ROLE_EMPLOYEE', 6);

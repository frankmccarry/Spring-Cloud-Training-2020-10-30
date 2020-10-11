INSERT INTO assignment (id, name) values(1, 'Rentokil');
INSERT INTO assignment (id, name) values(2, 'Oxford Brookes');
INSERT INTO assignment (id, name) values(3, 'Home Office');
INSERT INTO assignment (id, name) values(4, 'Photobox');

INSERT INTO skill (id, name, description) values(1, 'Spring', 'Java Framework');
INSERT INTO skill (id, name, description) values(2, 'Java', 'Programming language');
INSERT INTO skill (id, name, description) values(3, 'C#', 'Programming language');

INSERT INTO consultant (id, full_name, email, assignment_id) values (1, 'Leo Wrest', 'leo.wrest@paconsulting.com', 1);
INSERT INTO consultant (id, full_name, email, assignment_id) values (2, 'Alex Pritchard', 'alex.pritchard@paconsulting.com', 2);
INSERT INTO consultant (id, full_name, email) values (3, 'John Sharp', 'john.sharp@paconsulting.com');
INSERT INTO consultant (id, full_name, email) values (4, 'New Joiner', 'new.joiner@paconsulting.com');

INSERT INTO consultant_skills (consultant_id, skills_id) values(1, 1);
INSERT INTO consultant_skills (consultant_id, skills_id) values(1, 2);
INSERT INTO consultant_skills (consultant_id, skills_id) values(2, 1);
INSERT INTO consultant_skills (consultant_id, skills_id) values(2, 2);
INSERT INTO consultant_skills (consultant_id, skills_id) values(3, 3);
INSERT INTO consultant_skills (consultant_id, skills_id) values(4, 1);
INSERT INTO consultant_skills (consultant_id, skills_id) values(4, 2);
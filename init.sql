CREATE TABLE course_professor (
    courseID int,
    professorID int,
    PRIMARY KEY (courseID, professorID)
);

CREATE TABLE student_course (
    studentID int,
    courseID int,
    PRIMARY KEY (studentID, courseID)
);

INSERT INTO course_professor VALUES 
    (2, 1),
    (1, 2);

INSERT INTO student_course VALUES
    (1, 2),
    (1, 1),
    (2, 2),
    (2, 1),
    (3, 2),
    (4, 2);
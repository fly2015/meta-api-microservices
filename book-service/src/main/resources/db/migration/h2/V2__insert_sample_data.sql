-- Inserting test data into the Author table
INSERT INTO Author (name) VALUES
('J.K. Rowling'),
('Stephen King'),
('George R.R. Martin');

-- Inserting test data into the Genre table
INSERT INTO Genre (name) VALUES
('Fantasy'),
('Horror'),
('Science Fiction');

-- Inserting test data into the Book table
INSERT INTO Book (title, author_id, genre_id, isbn, publication_year) VALUES
('Harry Potter and the Philosopher''s Stone', 1, 1, '978-0-7475-3269-6', 1997),
('The Shining', 2, 2, '978-0-385-12167-5', 1977),
('A Game of Thrones', 3, 1, '978-0-553-89784-5', 1996);

-- Inserting test data into the Version table
INSERT INTO Version (book_id, edition_number, publication_date, format) VALUES
(1, 1, '1997-06-26', 'Hardcover'),
(2, 1, '1977-01-28', 'Paperback'),
(3, 1, '1996-08-06', 'E-book');

-- Inserting test data into the Book_Author table (many-to-many relationship)
INSERT INTO Book_Author (book_id, author_id) VALUES
(1, 1), -- Harry Potter and the Philosopher's Stone by J.K. Rowling
(2, 2), -- The Shining by Stephen King
(3, 3); -- A Game of Thrones by George R.R. Martin

/*-- Inserting test data into the Role table
INSERT INTO Roles (name) VALUES
('User'),
('Admin');

-- Inserting test data into the Users table
INSERT INTO Users (username, password, email, role_id) VALUES
('user1', 'password1', 'user1@example.com', 1),
('user2', 'password2', 'user2@example.com', 2);*/

INSERT INTO USERS (user_id, username, password, name, email, enabled, last_password_reset_date) VALUES
                                                                                               (1, 'admin', '$2a$10$zuI3P8hoZNkFGR2dDPW9juA1C1xIHBUNrKMGqjjaEKsLTwjJkKoNa', 'Admin', 'admin@gmail.com', true, CURRENT_TIMESTAMP()),
                                                                                               (2, 'siva', '$2a$10$LskLrNP6m.dEpXYjT41lRePseXJEjhd6.sPH2Z5GbbShtaFRWoeYq', 'Siva', 'siva@gmail.com', true, CURRENT_TIMESTAMP());

INSERT INTO ROLES (role_id, name) VALUES
                                 (1, 'ROLE_USER'),
                                 (2, 'ROLE_ADMIN');

INSERT INTO USER_ROLE (user_id, role_id) VALUES
                                             (1, 1),
                                             (2, 1),
                                             (2, 2);

-- Inserting test data into the Review table
INSERT INTO Review (user_id, book_id, rating, comment) VALUES
(1, 1, 5, 'Great book!'),
(2, 2, 4, 'Enjoyed it.');

-- Inserting test data into the Summary table
INSERT INTO Summary (book_id, content) VALUES
(1, 'The first book in the Harry Potter series.'),
(2, 'A psychological horror novel about a haunted hotel.');
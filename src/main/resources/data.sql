-- include de ROLES
INSERT INTO public.tb_roles(role_id, role_name) VALUES ('94b13bcb-81b9-45ab-bd43-16c3d1479d30', 'ROLE_STUDENT');
INSERT INTO public.tb_roles(role_id, role_name) VALUES ('c1386451-f4fb-444d-9c04-fe0cb2bcdee3', 'ROLE_INSTRUCTOR');
INSERT INTO public.tb_roles(role_id, role_name) VALUES ('c46e38a9-1228-4581-89bc-b00e976d6681', 'ROLE_ADMIN');
INSERT INTO public.tb_roles(role_id, role_name) VALUES ('f283323f-e7e1-4af1-bf05-ea2ccfd1cb0a', 'ROLE_USER');

-- include user master
INSERT INTO public.tb_users(user_id, cpf, creation_date, email, full_name, image_url,last_update_date, password, phone_number, user_status, user_type, username)
	VALUES ('10a6768f-404d-4adf-a0c8-432c56b17007', '32753455082', CURRENT_DATE, 'admin@silverfox.com', 'Administrator', 'http://silverfox.com', CURRENT_DATE, '$2a$10$mr68z529UoUL546X0xXECuTp.omfctGbVvfjHKv/2aYdzv.aU52Z.', '11 991298462', 'ACTIVE', 'ADMIN', 'master');

-- include USER ROLE
INSERT INTO public.tb_users_roles(user_id, role_id)
	VALUES ('10a6768f-404d-4adf-a0c8-432c56b17007', 'c46e38a9-1228-4581-89bc-b00e976d6681');

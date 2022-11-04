# BE-RecycleIT

---

This repo is used for keeping the backend of the RecycleIT application developed for the collective project.

## Local setup

---

### What you will need to install?

 - Docker desktop
 - You should have JDK 11 in IntelliJ (this is the JDK that we are going to use)
 - Make sure you have downloaded the Docker plugin for IntelliJ

### What steps should you follow in order to complete the project setup?

 - Once you clone this repo, you will see that there are 2 configurations in IntelliJ
   - Main - used to run the main service
   - Run docker image - used to create / run the mysql 8.0 docker image from IntelliJ
 - **Note:** If you run the "Run docker image" for the first time, you will have to wait around 3-5 minutes because Docker will have to download and initialize the MySQL image
 - To test if the Docker image is ready to be used, you can connect to the DB from IntelliJ Database plugin(you will find the credentials and the db link in the `application.yml`)

### Steps to follow in order to run the local setup

- Run the `Docker image` first
- Run the `Main configuration`

## Problems and solutions

---

Please write here any problem that you face + the solution for it.

## Q&A

---

### How does flyway work?

Flyway is a tool used to migrate db from a version to another. If you want to add 
a new version of the db you will have to follow the steps mentioned below:
- Write a sql script in the `resource/db/migration` folder
- The sql file should follow this naming convention `V1.{the next version number }__some-text`
- Check the `flyway_schema_history` table to see if your script ran successfully

**Note**  

- There can't be multiple sql scripts with the same version.

- In case of sql script failure, you will need to delete the record containing your script  
e.g.: If the V1.1__create_test_table.sql fails you will need to delete de record with version 1.1  


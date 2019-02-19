package ru.ifmo.web.client;

import ru.ifmo.web.database.entity.Astartes;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Client {
    public static void main(String... args) throws IOException {
        AstartesResourceIntegration astartesPort = new AstartesResourceIntegration();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int currentState = 0;

        while (true) {
            switch (currentState) {
                case 0:
                    System.out.println("\nВыберите один из пунктов:");
                    System.out.println("1. Вывести всех астартес");
                    System.out.println("2. Применить фильтры");
                    System.out.println("3. Создать");
                    System.out.println("4. Изменить");
                    System.out.println("5. Удалить");
                    System.out.println("6. Выйти");
                    currentState = readState(currentState, reader);
                    break;
                case 1:
                    System.out.println("Найдено:");
                    OperationResult<List<Astartes>> allResult = astartesPort.findAll();
                    if (allResult.isErr()) {
                        System.out.println(allResult.getErrorMessage());
                    } else {
                        allResult.getResult().stream().map(Client::astartesToString).forEach(System.out::println);
                    }
                    currentState = 0;
                    break;
                case 2:
                    System.out.println("\nЧтобы не применять фильтр, оставьте значение пустым");
                    System.out.println("id:");
                    Long id = readLong(reader);
                    System.out.println("name:");
                    String name = readString(reader);
                    System.out.println("title:");
                    String title = readString(reader);
                    System.out.println("position:");
                    String position = readString(reader);
                    System.out.println("planet:");
                    String planet = readString(reader);
                    System.out.println("birthdate(yyyy-mm-dd):");
                    Date birthdate = readDate(reader);
                    System.out.println("Найдено:");
                    OperationResult<List<Astartes>> result = astartesPort.findWithFilters(id, name, title, position, planet, birthdate);
                    if (result.isErr()) {
                        System.out.println(result.getErrorMessage());
                    } else {
                        result.getResult().stream().map(Client::astartesToString).forEach(System.out::println);
                    }
                    currentState = 0;
                    break;
                case 3:
                    System.out.println("\nЗаполните все поля");
                    String createName;
                    do {
                        System.out.println("name:");
                        createName = readString(reader);
                    } while (createName == null);
                    String createTitle;
                    do {
                        System.out.println("title:");
                        createTitle = readString(reader);
                    } while (createTitle == null);
                    String createPosition;
                    do {
                        System.out.println("position:");
                        createPosition = readString(reader);
                    } while (createPosition == null);
                    String createPlanet;
                    do {
                        System.out.println("planet:");
                        createPlanet = readString(reader);
                    } while (createPlanet == null);
                    Date createBirthdate;
                    do {
                        System.out.println("birthdate(yyyy-mm-dd):");
                        createBirthdate = readDate(reader);
                    } while (createBirthdate == null);
                    OperationResult<Long> longOperationResult = astartesPort.create(createName, createTitle, createPosition, createPlanet, createBirthdate);
                    if (longOperationResult.isErr()) {
                        System.out.println(longOperationResult.getErrorMessage());
                    } else {
                        System.out.println("ID новой записи: " + longOperationResult.getResult());
                    }
                    currentState = 0;
                    break;
                case 4:
                    Long updateId;
                    do {
                        System.out.println("id изменяемой записи (0 для отмены операции):");
                        updateId = readLong(reader);
                    } while (updateId == null);

                    if (updateId == 0L) {
                        currentState = 0;
                        break;
                    }
                    System.out.println("name:");
                    String updateName = readString(reader);
                    System.out.println("title:");
                    String updateTitle = readString(reader);
                    System.out.println("position:");
                    String updatePosition = readString(reader);
                    System.out.println("planet:");
                    String updatePlanet = readString(reader);
                    System.out.println("birthdate(yyyy-mm-dd):");
                    Date updateBirthdate = readDate(reader);
                    OperationResult<Integer> update = astartesPort.update(updateId, updateName, updateTitle, updatePosition, updatePlanet, updateBirthdate);
                    if (update.isErr()) {
                        System.out.println(update.getErrorMessage());
                    } else {
                        System.out.println("Изменено " + update.getResult() + " строк");

                    }
                    currentState = 0;
                    break;
                case 5:
                    Long deleteId;
                    do {
                        System.out.println("id удаляемой записи (0 для отмены операции):");
                        deleteId = readLong(reader);
                    } while (deleteId == null);
                    if (deleteId == 0L) {
                        currentState = 0;
                        break;
                    }
                    OperationResult<Integer> delete = astartesPort.delete(deleteId);
                    if (delete.isErr()) {
                        System.out.println(delete.getErrorMessage());
                    } else {
                        System.out.println("Удалено " + delete.getResult() + " строк");
                    }
                    currentState = 0;
                    break;
                case 6:
                    return;
                default:
                    currentState = 0;
                    break;
            }
        }
    }

    private static String readString(BufferedReader reader) throws IOException {
        String trim = reader.readLine().trim();
        if (trim.isEmpty()) {
            return null;
        }
        return trim;
    }

    private static Date readDate(BufferedReader reader) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            return sdf.parse(reader.readLine());
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    private static Long readLong(BufferedReader reader) {
        try {
            return Long.parseLong(reader.readLine());
        } catch (java.lang.Exception e) {
            return null;
        }
    }

    private static int readState(int current, BufferedReader reader) {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (java.lang.Exception e) {
            return current;
        }
    }

    private static String astartesToString(Astartes astartes) {
        return "Astartes(" +
                "id=" + astartes.getId() +
                ", name=" + astartes.getName() +
                ", title=" + astartes.getTitle() +
                ", position=" + astartes.getPosition() +
                ", planet=" + astartes.getPlanet() +
                ", birthdate=" + astartes.getBirthdate() +
                ")";
    }

}

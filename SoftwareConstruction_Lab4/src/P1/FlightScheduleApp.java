package P1;

import EntryState.State;
import Exceptions.*;
import HelperClasses.Location;
import HelperClasses.PlanningEntryAPIsStrategy;
import HelperClasses.TimePair;
import PlanningEntry.PlanningEntry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����1�Ŀͻ��˳���
 * �ɱ���
 */
public class FlightScheduleApp {
    private final List<FlightEntry> entries = new ArrayList<>();
    private final List<Plane> planes = new ArrayList<>();
    private final List<Location> locations = new ArrayList<>();
    final static Logger logger = Logger.getLogger(FlightScheduleApp.class.toString());
    private BufferedReader bfr = null;

    //AF���ƻ���Ϊentries���ɻ���ԴΪplanes��λ�ü�Ϊlocations�ĺ���������
    //RI��true
    //Safety from rep exposure����Ա��ʹ��private����

    public FlightScheduleApp() {
        //��logger��������
        Locale.setDefault(new Locale("en", "EN"));
        logger.setLevel(Level.INFO);
        try {
            FileHandler fileHandler = new FileHandler("src/P1/FlightScheduleLog.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ����������
     */
    public void runApp() {
        //ʶ���û��������
        printMenu();
        int operation = 0;
        Scanner scanner = new Scanner(System.in);
        //���о������
        while (true) {
            boolean isOperationSucceed = false;
            operation = scanner.nextInt();
            switch (operation) {
                case 1:
                    isOperationSucceed = addPlane();
                    break;
                case 2:
                    isOperationSucceed = addLocation();
                    break;
                case 3:
                    isOperationSucceed = addEntry();
                    break;
                case 4:
                    isOperationSucceed = cancelEntry();
                    break;
                case 5:
                    isOperationSucceed = allocateSource();
                    break;
                case 6:
                    isOperationSucceed = runEntry();
                    break;
                case 7:
                    isOperationSucceed = endEntry();
                    break;
                case 8:
                    isOperationSucceed = getEntryState();
                    break;
                case 9:
                    isOperationSucceed = checkResourceConflict();
                    break;
                case 10:
                    isOperationSucceed = getEntriesUseInputResourceAndPreEntry();
                    break;
                case 11:
                    isOperationSucceed = showInfoBoard();
                    break;
                case 12:
                    isOperationSucceed = deleteLocation();
                    break;
                case 13:
                    isOperationSucceed = deletePlane();
                    break;
                case 14:
                    isOperationSucceed = getEntriesFromFile();
                    break;
                case 15:
                    isOperationSucceed = showLog();
                    break;
                default:
                    return;
            }
            if (isOperationSucceed) {
                System.out.println("�����ɹ�\n");
            } else {
                System.out.println("����ʧ��\n");
            }
            printMenu();
        }
    }

    /**
     * 1.Ϊ�������һ���µķɻ�
     *
     * @return �Ƿ���ӳɹ�
     */
    private boolean addPlane() {
        System.out.println("������ɻ��ı�ţ��ͺţ���λ���ͻ���");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.next();
        String model = scanner.next();
        int seat = scanner.nextInt();
        double year = scanner.nextDouble();
        Plane inputPlane = new Plane(code, model, seat, year);

        try {
            if (planes.contains(inputPlane)) {
                throw new RepeatedAdditionException("������Ϊ" + code + "�ķɻ��Ѵ���");
            } else {
                planes.add(inputPlane);
                logger.info("��ӱ��Ϊ" + code + "�ķɻ��ɹ�");
                return true;
            }
        } catch (RepeatedAdditionException e) {
            e.printStackTrace();
            logger.warning(e.getMessage());
            return false;
        }

    }

    /**
     * 2.Ϊ�������һ���µĵص�
     *
     * @return �Ƿ���ӳɹ�
     */
    private boolean addLocation() {
        System.out.println("������λ��");
        Location inputLocation = new Location(new Scanner(System.in).next());

        try {
            if (locations.contains(inputLocation)) {
                throw new RepeatedAdditionException("�����Ϊ" + inputLocation.getLocationName() + "�ĵص��ظ�");
            } else {
                locations.add(inputLocation);
                logger.info("�����Ϊ" + inputLocation.getLocationName() + "�ĵص�ɹ�");
                return true;
            }
        } catch (RepeatedAdditionException e) {
            e.printStackTrace();
            logger.warning(e.getMessage());
            return false;
        }
    }

    /**
     * 3.Ϊ�������һ���µļƻ���
     *
     * @return �Ƿ���ӳɹ�
     */
    private boolean addEntry() {
        //���üƻ�����
        Scanner scanner = new Scanner(System.in);
        System.out.println("�������½��ƻ�����");
        String entryName = scanner.next();
        FlightEntry entry = new FlightEntry(entryName);

        //����ʱ��
        boolean isSetTimeSucceed = setEntryTime(entry);
        if (!isSetTimeSucceed) {
            return false;
        }

        //����λ��
        boolean isSetLocationSucceed = setEntryLocation(entry);
        if (!isSetLocationSucceed) {
            return false;
        }

        //����ƻ�������ʱ����Ѵ��ڵ�ĳ�ƥ�䣬�����ʧ��
        for (FlightEntry temp : entries) {
            try {
                temp.checkNameAndTimeCompatible(entry);
            } catch (SameTagException e) {
                logger.warning(e.getMessage());
                e.printStackTrace();
                return false;
            } catch (ElementRelationException e) {
                logger.warning(e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        logger.info("��Ӽƻ���" + entry.getPlanningEntryName() + "�ɹ�");
        entries.add(entry);
        return true;
    }

    /**
     * 3.Ϊ�¼ƻ�������ʱ��
     *
     * @param inputEntry ��Ҫ���õļƻ���
     * @return �Ƿ����óɹ�
     */
    private boolean setEntryTime(FlightEntry inputEntry) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("��������ʼʱ�䣬��ʽyyyy-MM-dd HH:mm");
        String startTime = scanner.nextLine();
        System.out.println("���������ʱ�䣬��ʽyyyy-MM-dd HH:mm");
        String endTime = scanner.nextLine();

        //��������Ƿ���Ϲ���
        try {
            if ((!checkTimeFormat(startTime)) || (!checkTimeFormat(endTime))) {
                logger.warning("�����ƻ���" + inputEntry.getPlanningEntryName() + "ʱ����ʱ���ʽ����");
                return false;
            }
            if (TimePair.compareTime(startTime, endTime) != 1) {
                throw new WrongTimeFormatException("�����ƻ���" + inputEntry.getPlanningEntryName() + "ʱ������ʼʱ��������ֹʱ��");
            }
        } catch (WrongTimeFormatException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        //����ʱ������
        TimePair timePair = new TimePair(startTime, endTime);
        List<TimePair> time = new ArrayList<>();
        time.add(timePair);
        inputEntry.presetTime(time);
        return true;
    }

    /**
     * 3.Ϊ�¼ƻ������õص�
     *
     * @param inputEntry ��Ҫ���õļƻ���
     * @return �Ƿ����óɹ�
     */
    private boolean setEntryLocation(FlightEntry inputEntry) {
        Scanner scanner = new Scanner(System.in);
        printLocations();
        System.out.println("��������ʼ�͵���ص���");
        int from = scanner.nextInt();
        int to = scanner.nextInt();

        //����������Ƿ���Ϸ�Χ
        if (!isLegalLocationNumber(from) || !isLegalLocationNumber(to)) {
            logger.warning("Ϊ�ƻ���" + inputEntry.getPlanningEntryName() + "����λ��ʱ������ų�����Χ");
            return false;
        }

        //�������ص��Ƿ��ظ�
        try {
            if (from == to) {
                throw new SetLocationException("�����ƻ���" + inputEntry.getPlanningEntryName() + "ʱ����ص��ظ�");
            }
        } catch (SetLocationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        //����λ��
        Location fromLocation = locations.get(from);
        Location toLocation = locations.get(to);
        List<Location> locationList = new ArrayList<>();
        locationList.add(fromLocation);
        locationList.add(toLocation);
        inputEntry.presetLocation(locationList);
        return true;

    }

    /**
     * 4.ȡ��ĳ�ƻ���
     *
     * @return �Ƿ�ȡ���ɹ�
     */
    private boolean cancelEntry() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("��������Ҫȡ���ļƻ�����");
        printEntries();
        int opNumber = scanner.nextInt();

        //�������ƻ������Ƿ�Ϸ�
        if (!isLegalEntryNumber(opNumber)) {
            return false;
        }

        try {
            if (entries.get(opNumber).cancel()) {
                logger.info("ȡ���ƻ���" + entries.get(opNumber).getPlanningEntryName() + "�ɹ�");
                return true;
            } else {
                throw new IllegalOperationException("ȡ���ƻ���" + entries.get(opNumber).getPlanningEntryName() + "ʧ��");
            }
        } catch (IllegalOperationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 5.Ϊ�ƻ��������Դ
     *
     * @return �Ƿ����ɹ�
     */
    private boolean allocateSource() {
        System.out.println("��������Ҫ����ļƻ�����");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();

        //��������Ƿ��ڷ�Χ��
        if (!isLegalEntryNumber(opNumber)) {
            logger.warning("������Դʱѡ��ƻ���ʧ��");
            return false;
        }

        //����Ƿ�����ɷ����Ҫ��
        FlightEntry opEntry = entries.get(opNumber);
        try {
            if (opEntry.getState() != State.WAITING) {
                throw new IllegalOperationException("����" + opEntry.getPlanningEntryName() + "�����ڴ�����״̬�����з���ʧ��");
            }
        } catch (IllegalOperationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        printPlanes();
        System.out.println("��������Ҫ�������Դ���");
        int sourceNumber = scanner.nextInt();
        //��������Ƿ��ڷ�Χ��
        if (!isLegalPlaneNumber(sourceNumber)) {
            logger.warning("Ϊ����" + opEntry.getPlanningEntryName() + "������Դʱ��������Դ��ų�����Χ");
            return false;
        }
        Plane tempPlane = planes.get(sourceNumber);

        //����Ƿ������Դ��ռ��ͻ
        List<Plane> list = new ArrayList<>();
        list.add(tempPlane);
        opEntry.setSource(list);
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy<>(1);
        try {
            if (api.checkResource(entries)) {
                //������ڳ�ͻ���׳��쳣
                list = new ArrayList<>();
                opEntry.setSource(list);
                throw new AllocateSourceException("Ϊ����" + opEntry.getPlanningEntryName() + "������Դʱ������Դ��ռ��ͻ");
            }
        } catch (AllocateSourceException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
        logger.info("Ϊ�ƻ���" + opEntry.getPlanningEntryName() + "������Դ" + tempPlane.getCode() + "�ɹ�");
        return true;
    }

    /**
     * 6.����ĳ���ƻ���
     *
     * @return �Ƿ������ɹ�
     */
    private boolean runEntry() {
        System.out.println("��������Ҫ�����ļƻ������");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        //��������Ƿ��ڷ�Χ��
        if (!isLegalEntryNumber(opEntryNumber)) {
            logger.warning("ѡ��ƻ���ʱ������Χ");
            return false;
        }
        FlightEntry opEntry = entries.get(opEntryNumber);
        try {
            if (opEntry.run()) {
                logger.info("�����ƻ���" + opEntry.getPlanningEntryName() + "�ɹ�");
                return true;
            } else {
                throw new IllegalOperationException("���������㣬�޷������ƻ���" + opEntry.getPlanningEntryName());
            }
        } catch (IllegalOperationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 7.����һ���ƻ���
     *
     * @return �Ƿ�����ɹ�
     */
    private boolean endEntry() {
        System.out.println("��������Ҫ�����ļƻ������");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();

        //��������Ƿ��ڷ�Χ��
        if (!isLegalEntryNumber(opEntryNumber)) {
            logger.warning("ѡ��ƻ���ʱ������Χ");
            return false;
        }

        FlightEntry opEntry = entries.get(opEntryNumber);

        try {
            if (opEntry.end()) {
                logger.info("�����ƻ���" + opEntry.getPlanningEntryName() + "�ɹ�");
                return true;
            } else {
                throw new IllegalOperationException("�����ƻ���" + opEntry.getPlanningEntryName() + "ʱ���������㣬�޷�����");
            }
        } catch (IllegalOperationException e) {
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 8.��ȡ�ƻ���״̬
     *
     * @return �Ƿ��ѯ�ɹ�
     */
    private boolean getEntryState() {
        printEntries();
        Scanner scanner = new Scanner(System.in);
        System.out.println("��������Ҫ��ȡ��Ŀ�ı��");
        int input = scanner.nextInt();

        //������Ƿ�Ϸ�
        if (!isLegalEntryNumber(input)) {
            logger.warning("ѡ��ƻ���ʱ������Χ");
            return false;
        } else {
            FlightEntry tempEntry = entries.get(input);
            System.out.println("�üƻ����״̬�ǣ�" + tempEntry.getStatusToCharacters());
            logger.info("��ѯ�ƻ���" + tempEntry.getPlanningEntryName() + "��״̬�ɹ�");
            return true;
        }
    }

    /**
     * 9.����Ƿ������Դ��ͻ
     *
     * @return �����Ƿ�ɹ�
     */
    private boolean checkResourceConflict() {
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy(1);
        if (api.checkResource(entries)) {
            System.out.println("������Դ�����ͻ");
        } else {
            System.out.println("��������Դ�����ͻ");
        }
        logger.info("������мƻ����Ƿ������Դ��ͻ");
        return true;
    }

    /**
     * 10.��ȡʹ��ָ����Դ�����мƻ���Լ�ָ����Ŀ��ǰ��ƻ���
     *
     * @return �Ƿ�����ɹ�
     */
    private boolean getEntriesUseInputResourceAndPreEntry() {
        printPlanes();
        System.out.println("��������Ҫ��ѯ�ķɻ����");
        Scanner scanner = new Scanner(System.in);
        int inputPlaneNumber = scanner.nextInt();
        if (!isLegalPlaneNumber(inputPlaneNumber)) {
            logger.warning("ѡ����Դʱ������Χ");
            return false;
        }
        Plane targetPlane = planes.get(inputPlaneNumber);
        List<FlightEntry> targetEntries = new ArrayList<>();
        //����Ѱ��ʹ��ָ����Դ�ļƻ������ӡ
        for (FlightEntry tempEntry : entries) {
            if (tempEntry.getSource().get(0).equals(targetPlane)) {
                targetEntries.add(tempEntry);
            }
        }
        System.out.println("ʹ��ָ����Դ�ļƻ�������");
        for (int i = 0; i < targetEntries.size(); i++) {
            System.out.println(i + "." + targetEntries.get(i).toString());
        }

        //Ѱ��ָ���ƻ����ǰ��ƻ���
        System.out.println("����ָ���ƻ���ı��");
        int inputEntryNumber = scanner.nextInt();
        if (inputEntryNumber < 0 || inputEntryNumber >= targetEntries.size()) {
            logger.warning("ѡ��ƻ���ʱ������Χ");
            System.out.println("������ų�����Χ");
            return false;
        }
        FlightEntry inputEntry = targetEntries.get(inputEntryNumber);
        PlanningEntry<Plane> preEntry = null;
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy<>(1);
        preEntry = api.findPreSameResourceEntry(targetPlane, inputEntry, targetEntries);
        if (preEntry == null) {
            System.out.println("��ǰ��ƻ���");
        } else {
            System.out.println("ǰ��ƻ������£�");
            FlightEntry tempEntry = (FlightEntry) preEntry;
            System.out.println(tempEntry.toString());
        }
        logger.info("��ȡʹ��" + targetPlane.getCode() + "�����мƻ���Լ�" + inputEntry.getPlanningEntryName() + "��ǰ��ƻ���ɹ�");
        return true;
    }

    /**
     * 11.��ʾָ��λ�õ���Ϣ��
     *
     * @return �Ƿ���ʾ�ɹ�
     */
    private boolean showInfoBoard() {
        printLocations();
        System.out.println("��������Ҫ��ѯ��λ��");
        Scanner scanner = new Scanner(System.in);
        int inputLocationNumber = scanner.nextInt();
        if (!isLegalLocationNumber(inputLocationNumber)) {
            logger.warning("ѡ��λ��ʱ������Χ");
            return false;
        }
        String locationName = locations.get(inputLocationNumber).getLocationName();
        logger.info("��ѯ" + locationName + "�ĺ�����Ϣ��ɹ�");
        new FlightBoard(locationName, entries);
        return true;
    }

    /**
     * 12.ɾ��ָ��λ��
     *
     * @return �Ƿ�ɾ���ɹ�
     */
    private boolean deleteLocation() {
        printLocations();
        System.out.println("��������Ҫɾ����λ�ñ��");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if (isLegalLocationNumber(opNumber)) {
            //����Ƿ���ռ�ø�λ�õ�δ��������
            Location opLocation = locations.get(opNumber);
            for (FlightEntry tempEntry : entries) {
                if (tempEntry.getLocation().get(0).equals(opLocation) || tempEntry.getLocation().get(1).equals(opLocation)) {
                    try {
                        if (tempEntry.getState() != State.ENDED && tempEntry.getState() != State.CANCELLED) {
                            throw new DeleteLocationException("��ɾ���ص�" + opLocation.getLocationName() +
                                    "��δ��������" + tempEntry.getPlanningEntryName() + "ռ��");
                        }
                    } catch (DeleteLocationException e) {
                        logger.warning(e.getMessage());
                        e.printStackTrace();
                        return false;
                    }

                }
            }
            //����ռ����ֱ��ɾ��
            logger.info("ɾ��λ��" + opLocation.getLocationName() + "�ɹ�");
            locations.remove(opNumber);
            return true;
        } else {
            logger.warning("ѡ��λ��ʱ������Χ");
            System.out.println("������ų�����Χ");
            return false;
        }
    }

    /**
     * 13.ɾ��ָ����Դ
     *
     * @return �Ƿ�ɾ���ɹ�
     */
    private boolean deletePlane() {
        printPlanes();
        System.out.println("��������Ҫɾ���ķɻ����");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if (isLegalPlaneNumber(opNumber)) {
            //���÷ɻ��Ƿ�δ�����ĺ���ռ��
            Plane opPlane = planes.get(opNumber);
            for (FlightEntry tempEntry : entries) {
                //�����ǰ�ƻ���û�з�����Դ���������ѭ��
                if (tempEntry.getSource().size() == 0) {
                    continue;
                }
                //���ǣ�浽��ɾ����Դ�����״̬
                if (tempEntry.getSource().get(0).equals(opPlane)) {
                    try {
                        if (tempEntry.getState() != State.CANCELLED && tempEntry.getState() != State.ENDED) {
                            throw new DeleteSourceException("����ռ�÷ɻ�" + opPlane.getCode() + "��δ��������" + tempEntry.getPlanningEntryName());
                        }
                    } catch (DeleteSourceException e) {
                        logger.warning(e.getMessage());
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            //����Դռ�ã�ɾ����Ӧ�ɻ�����
            logger.info("ɾ���ɻ�" + opPlane.getCode() + "�ɹ�");
            planes.remove(opNumber);
            return true;
        } else {
            logger.warning("ѡ����Դʱ������Χ");
            System.out.println("������ų�����Χ");
            return false;
        }
    }

    /**
     * 14.���ļ��л�ȡ�ƻ���
     *
     * @return �����Ƿ�ɹ�
     * @throws FileNotFoundException δ�ҵ�Ŀ���ļ��׳��쳣
     */
    private boolean getEntriesFromFile() {
        System.out.println("������1-5֮��ı��");
        Scanner scanner = new Scanner(System.in);
        String opNum = scanner.next();
        String filePath = "src/Files/FlightSchedule_" + opNum + ".txt";
        EntriesFromFile entriesFromFile = new EntriesFromFile(filePath);

        //��ӡ�Ѽ���ļƻ���
        System.out.println("�Ѿ�����ļƻ������£�");
        entriesFromFile.printEntries();

        //����ȡ�ļ��еļƻ����Ԫ�أ����ࡢ�ص㡢�ɻ������뵽APP��List��
        for (FlightEntry temp : entriesFromFile.getEntries()) {
            entries.add(temp);
        }
        for (Plane tempPlane : entriesFromFile.getPlanes()) {
            planes.add(tempPlane);
        }
        for (Location tempLocation : entriesFromFile.getLocations()) {
            locations.add(tempLocation);
        }
        return true;
    }

    /**
     * 15.��ʾ�������е���־��¼
     *
     * @return �Ƿ���ʾ�ɹ�
     */
    private boolean showLog() {
        try {
            bfr = new BufferedReader(new FileReader("src/P1/FlightScheduleLog.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //֧�ְ�����Ϣ���͡��������͡�ʱ�����
        System.out.println("�����밴�պ�����������");
        System.out.println("1.������Ϣ����");
        System.out.println("2.���ղ�������");
        System.out.println("3.����ʱ��");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextInt()) {
            case 1:
                return searchByInfoType();
            case 2:
                return searchByOperationType();
            case 3:
                return searchByTime();
            default:
                System.out.println("���������������²���");
                return false;
        }
    }

    /**
     * 15.������־��Ϣ���Ͳ���
     *
     * @return �Ƿ���ҳɹ�
     */
    private boolean searchByInfoType() {
        System.out.println("��������Ҫ���ҵ���Ϣ����: 1.INFO 2.WARNING 3.SEVERE");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if (opNumber != 1 && opNumber != 2 && opNumber != 3) {
            System.out.println("������Ϣ���������²���");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P1\\.(.*?)\\n([A-Z]+): (.*?)");
        while (opLogString != null) {
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if (opNumber == 1 && matcher.group(3).equals("INFO")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\tλ�ã�" + matcher.group(2) + "\t���ݣ�" + matcher.group(4));
            } else if (opNumber == 2 && matcher.group(3).equals("WARNING")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\tλ�ã�" + matcher.group(2) + "\t���ݣ�" + matcher.group(4));
            } else if (opNumber == 3 && matcher.group(3).equals("SEVERE")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\tλ�ã�" + matcher.group(2) + "\t���ݣ�" + matcher.group(4));
            }
            opLogString = readSingleLog();
        }
        return true;
    }

    /**
     * 15.������־�������Ͳ���
     *
     * @return �Ƿ���ҳɹ�
     */
    private boolean searchByOperationType() {
        System.out.println("��������Ҫ���ҵ���Ϣ����:");
        System.out.println("1.����һ�ܷɻ�");
        System.out.println("2.����һ���ص�");
        System.out.println("3.����һ���ƻ���");
        System.out.println("4.ȡ��ĳ���ƻ���");
        System.out.println("5.Ϊĳ���ƻ��������Դ");
        System.out.println("6.����ĳ���ƻ���");
        System.out.println("7.����ĳ���ƻ���");
        System.out.println("8.��ѯĳ�ƻ����״̬");
        System.out.println("9.����Ѵ��ڼƻ����Ƿ������Դ��ͻ");
        System.out.println("10.��ȡʹ��ָ����Դ�����мƻ���Լ�ָ����Ŀ��ǰ��ƻ���");
        System.out.println("11.��ʾָ��λ�û�������Ϣ��");
        System.out.println("12.ɾ��ָ��λ��");
        System.out.println("13.ɾ��ָ����Դ");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if (opNumber < 1 || opNumber > 13) {
            System.out.println("������Ϣ���������²���");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P1\\.([A-Z||a-z]+)\\s([A-Z||a-z]+)\\n([A-Z]+): (.*?)");
        while (opLogString != null) {
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if (opNumber == 1 && matcher.group(3).equals("addPlane")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 2 && matcher.group(3).equals("addLocation")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 3 && matcher.group(3).equals("addEntry")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 4 && matcher.group(3).equals("cancelEntry")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 5 && matcher.group(3).equals("allocateSource")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 6 && matcher.group(3).equals("runEntry")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 7 && matcher.group(3).equals("endEntry")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 8 && matcher.group(3).equals("getEntryState")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 9 && matcher.group(3).equals("checkResourceConflict")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 10 && matcher.group(3).equals("getEntriesUseInputResourceAndPreEntry")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 11 && matcher.group(3).equals("showInfoBoard")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 12 && matcher.group(3).equals("deleteLocation")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            } else if (opNumber == 13 && matcher.group(3).equals("deletePlane")) {
                System.out.println("ʱ�䣺" + matcher.group(1) + "\t������" + matcher.group(3) + "\t���ݣ�" + matcher.group(5));
            }
            opLogString = readSingleLog();
        }
        return true;
    }

    /**
     * 15.������־ʱ�����
     *
     * @return �Ƿ���ҳɹ�
     */
    private boolean searchByTime() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("��������ʼʱ�䣬��ʽyyyy-MM-dd HH:mm");
        String startTime = scanner.nextLine();
        System.out.println("���������ʱ�䣬��ʽyyyy-MM-dd HH:mm");
        String endTime = scanner.nextLine();

        //��������Ƿ���Ϲ���
        if ((!checkTimeFormat(startTime)) || (!checkTimeFormat(endTime))) {
            return false;
        }
        if (TimePair.compareTime(startTime, endTime) != 1) {
            System.out.println("������ʼʱ����ڻ�������ֹʱ��");
            return false;
        }

        Pattern pattern = Pattern.compile("(20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}):\\d{2}\\sP1\\.(.*?)\\n(.*?)");
        String opString = readSingleLog();

        while (opString!=null){
            Matcher matcher = pattern.matcher(opString);
            matcher.matches();
            String tempTime = matcher.group(1);
            if(TimePair.compareTime(startTime, tempTime)==1 && TimePair.compareTime(endTime, tempTime)==2){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\tλ�ã�"+matcher.group(2)+"\t��Ϣ��"+matcher.group(3));
            }
            opString = readSingleLog();
        }
        return true;
    }

    /**
     * 15.ÿ�ζ�ȡ��־��һ����У�
     *
     * @return ��ȡ������
     */
    private String readSingleLog() {
        try {
            String stringToReturn = bfr.readLine();
            if (stringToReturn == null) {
                return null;
            } else {
                stringToReturn += "\n" + bfr.readLine();
                return stringToReturn;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ��鵥����ʱ���Ƿ����"yyyy-MM-dd HH:mm"�ĸ�ʽ
     *
     * @param inputTime ��Ҫ����ʱ��
     * @return ��ʱ���Ƿ���ϸ�ʽ
     */
    private boolean checkTimeFormat(String inputTime) {
        Pattern pattern = Pattern.compile("20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(inputTime);
        try {
            if (matcher.matches()) {
                return true;
            } else {
                throw new WrongTimeFormatException("����ʱ�䲻���Ϲ���");
            }
        } catch (WrongTimeFormatException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * ������������Ƿ���entries��Χ��
     *
     * @param num ��Ҫ��������
     * @return �Ƿ��ڷ�Χ��
     */
    private boolean isLegalEntryNumber(int num) {
        try {
            if (num >= 0 && num < entries.size()) {
                return true;
            } else {
                throw new InputOutOfBoundException("����ƻ����ų�����Χ");
            }
        } catch (InputOutOfBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ������������Ƿ���planes��Χ��
     *
     * @param num ��Ҫ��������
     * @return �Ƿ��ڷ�Χ��
     */
    private boolean isLegalPlaneNumber(int num) {
        try {
            if (num >= 0 && num < planes.size()) {
                return true;
            } else {
                throw new InputOutOfBoundException("����ɻ���ų�����Χ");
            }
        } catch (InputOutOfBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ������������Ƿ���locations��Χ��
     *
     * @param num ��Ҫ��������
     * @return �Ƿ���locations��Χ��
     */
    private boolean isLegalLocationNumber(int num) {
        try {
            if (num >= 0 && num < locations.size()) {
                return true;
            } else {
                throw new InputOutOfBoundException("����λ�ñ�ų�����Χ");
            }
        } catch (InputOutOfBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ��ʾ�˵�ѡ��
     */
    private void printMenu() {
        System.out.println("��ӭʹ�ú����������������Ӧ�����ɲ���");
        System.out.println("1.����һ�ܷɻ�");
        System.out.println("2.����һ���ص�");
        System.out.println("3.����һ���ƻ���");
        System.out.println("4.ȡ��ĳ���ƻ���");
        System.out.println("5.Ϊĳ���ƻ��������Դ");
        System.out.println("6.����ĳ���ƻ���");
        System.out.println("7.����ĳ���ƻ���");
        System.out.println("8.��ѯĳ�ƻ����״̬");
        System.out.println("9.����Ѵ��ڼƻ����Ƿ������Դ��ͻ");
        System.out.println("10.��ȡʹ��ָ����Դ�����мƻ���Լ�ָ����Ŀ��ǰ��ƻ���");
        System.out.println("11.��ʾָ��λ�û�������Ϣ��");
        System.out.println("12.ɾ��ָ��λ��");
        System.out.println("13.ɾ��ָ����Դ");
        System.out.println("14.���ļ��ж�ȡ�ƻ���");
        System.out.println("15.������־");
        System.out.println("16.�˳�����");
    }

    /**
     * ��ʾ�Ѵ��ڵļƻ�����Ϣ���������
     */
    private void printEntries() {
        for (int i = 0; i < entries.size(); i++) {
            FlightEntry tempEntry = entries.get(i);
            System.out.println(i + "." + tempEntry.toString());
        }
    }

    /**
     * ��ʾ�Ѵ��ڵķɻ���Ϣ
     */
    private void printPlanes() {
        for (int i = 0; i < planes.size(); i++) {
            Plane tempPlane = planes.get(i);
            System.out.println(i + "." + tempPlane.toString());
        }
    }

    /**
     * ��ʾ�������λ����Ϣ
     */
    private void printLocations() {
        for (int i = 0; i < locations.size(); i++) {
            Location tempLocation = locations.get(i);
            System.out.println(i + "." + tempLocation.getLocationName());
        }
    }

    public static void main(String argv[]) {
        new FlightScheduleApp().runApp();
    }
}

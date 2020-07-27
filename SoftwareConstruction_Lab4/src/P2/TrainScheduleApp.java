package P2;

import EntryState.State;
import Exceptions.*;
import HelperClasses.Location;
import HelperClasses.PlanningEntryAPIsStrategy;
import HelperClasses.TimePair;
import P1.Plane;

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
 * ����2�Ŀͻ��˳���
 * �ɱ���
 */
public class TrainScheduleApp {
    private final List<TrainEntry> entries = new ArrayList<>();
    private final List<Train> trains = new ArrayList<>();
    private final List<Location> locations = new ArrayList<>();
    final static Logger logger = Logger.getLogger(TrainScheduleApp.class.toString());
    private BufferedReader bfr = null;

    //AF���ƻ���Ϊentries���г���ԴΪtrains��λ�ü�Ϊlocations���г��������
    //RI��true
    //Safety from rep exposure����Ա��ʹ��private����

    public TrainScheduleApp(){
        //��logger��������
        Locale.setDefault(new Locale("en", "EN"));
        logger.setLevel(Level.INFO);
        try{
            FileHandler fileHandler = new FileHandler("src/P2/TrainScheduleLog.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �ͻ�����������
     */
    public void runApp(){
        //ʶ���û��������
        printMenu();
        int operation = 0;
        Scanner scanner = new Scanner(System.in);
        //���о������
        while (true){
            boolean isOperationSucceed = false;
            operation = scanner.nextInt();
            switch (operation){
                case 1:
                    isOperationSucceed = addTrain();
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
                    isOperationSucceed = blockEntry();
                    break;
                case 8:
                    isOperationSucceed = endEntry();
                    break;
                case 9:
                    isOperationSucceed = getEntryState();
                    break;
                case 10:
                    isOperationSucceed = checkResourceConflict();
                    break;
                case 11:
                    isOperationSucceed = getEntriesUseInputResourceAndPreEntry();
                    break;
                case 12:
                    isOperationSucceed = showInfoBoard();
                    break;
                case 13:
                    isOperationSucceed = deleteLocation();
                    break;
                case 14:
                    isOperationSucceed = deleteTrain();
                    break;
                case 15:
                    isOperationSucceed = showLog();
                    break;
                default:
                    return;
            }
            if(isOperationSucceed){
                System.out.println("�����ɹ�\n");
            }
            else {
                System.out.println("����ʧ��\n");
            }
            printMenu();
        }
    }

    /**
     * 1.Ϊ�������һ���µĳ���
     * @return �Ƿ���ӳɹ�
     */
    private boolean addTrain(){
        System.out.println("�����복��ı�ţ����ͣ���λ���ͻ���");
        Scanner scanner = new Scanner(System.in);
        int code = scanner.nextInt();
        String model = scanner.next();
        int seat = scanner.nextInt();
        double year = scanner.nextDouble();
        Train inputTrain = new Train(code, model, seat, year);

        //��������Ƿ��Ѿ�����
        try{
            if(trains.contains(inputTrain)){
                throw new RepeatedAdditionException("���Ϊ"+code+"�ĳ����Ѵ���");
            }
        }
        catch (RepeatedAdditionException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        trains.add(inputTrain);
        logger.info("���Ϊ"+code+"�ĳ��������");
        return true;
    }

    /**
     * 2.Ϊ�������һ���µĵص�
     * @return �Ƿ���ӳɹ�
     */
    private boolean addLocation(){
        System.out.println("������λ��");
        Location inputLocation = new Location(new Scanner(System.in).next());

        //��������Ƿ��Ѿ�����
        try{
            if(locations.contains(inputLocation)){
                throw new RepeatedAdditionException("����λ��"+inputLocation.getLocationName()+"�Ѵ���");
            }
        }
        catch (RepeatedAdditionException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        locations.add(inputLocation);
        logger.info("����λ��"+inputLocation.getLocationName()+"�����");
        return true;
    }

    /**
     * 3.���һ���¼ƻ���
     * @return �Ƿ���ӳɹ�
     */
    private boolean addEntry(){
        System.out.println("�����복��");
        Scanner scanner = new Scanner(System.in);
        String entryName = scanner.next();
        TrainEntry entry = new TrainEntry(entryName);
        if(setEntryTime(entry) && setEntryLocation(entry)){
            //����ƻ�������ʱ����Ѵ��ڵ�ĳ�ƥ�䣬�����ʧ��
            for(TrainEntry temp : entries){
                try{
                    if(!temp.isNameAndTimeCompatible(entry)){
                        throw new ElementRelationException(entryName+"��ĳ�Ѵ��ڼƻ����ͻ");
                    }
                }
                catch (ElementRelationException e){
                    logger.warning(e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }
            logger.info("�ƻ���"+entryName+"��ӳɹ�");
            entries.add(entry);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 3.Ϊ�¼ƻ�������ʱ��
     * @param entry ��Ҫ���õļƻ���
     * @return �Ƿ����óɹ�
     */
    private boolean setEntryTime(TrainEntry entry){
        Scanner scanner = new Scanner(System.in);
        List<TimePair> timePairList = new ArrayList<>();
        System.out.println("������ʱ��Եĸ���");
        int timePairNumber = scanner.nextInt();
        scanner.nextLine();//����س�

        try{
            if(timePairNumber+1 > locations.size()){
                throw new InputOutOfBoundException("����ӵص������㣬���üƻ���"+entry.getPlanningEntryName()+"��ʱ��ʧ��");
            }
        }
        catch (InputOutOfBoundException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        System.out.println("������ʱ�䣬��ʽyyyy-MM-dd HH:mm��ÿ����һ������ʱ����һ�»س���");
        for(int i=0 ; i<timePairNumber ; i++){
            String startTime = scanner.nextLine();
            String endTime = scanner.nextLine();
            //��������ʽ�Ƿ�����Ҫ��
            try{
                if(!isLegalTimeFormat(startTime) || !isLegalTimeFormat(endTime)){
                    logger.warning("Ϊ�ƻ���"+entry.getPlanningEntryName()+"����ʱ��ʱ�����ʽ����");
                    return false;
                }
                if(TimePair.compareTime(startTime, endTime)!=1){
                    throw new WrongTimeFormatException("�����ƻ���"+entry.getPlanningEntryName()+"ʱ������ʼʱ�����ڵ���ʱ��");
                }
            }
            catch (WrongTimeFormatException e){
                logger.warning(e.getMessage());
                e.printStackTrace();
                return false;
            }
            TimePair inputTime = new TimePair(startTime, endTime);
            timePairList.add(inputTime);
        }
        entry.presetTime(timePairList);
        return true;
    }

    /**
     * 3.Ϊ�¼ƻ�������λ��
     * @param entry �½��ƻ���
     * @return �Ƿ����óɹ�
     */
    private boolean setEntryLocation(TrainEntry entry){
        int locationNumber = entry.getTime().size()+1;
        printLocations();
        System.out.println("�ܹ���Ҫ���"+locationNumber+"���ص㣬������������ţ������ظ�");
        Scanner scanner = new Scanner(System.in);
        List<Location> locationList = new ArrayList<>();
        List<Integer> inputNumbers = new ArrayList<>();
        for(int i=0 ; i<locationNumber ; i++){
            int input = scanner.nextInt();

            try{
                if(inputNumbers.contains(input)){
                    throw new RepeatedAdditionException("����ص�"+locations.get(input).getLocationName()+"�ظ�");
                }
                if(!isLegalLocationNumber(input)){
                    logger.warning("����ص���ų�����Χ");
                }
            }
            catch (RepeatedAdditionException e){
                logger.warning(e.getMessage());
                e.printStackTrace();
                return false;
            }

            inputNumbers.add(input);
            Location tempLocation = locations.get(input);
            locationList.add(tempLocation);
        }
        entry.presetLocation(locationList);
        return true;
    }

    /**
     * 4.ȡ��ĳ�ƻ���
     * @return �Ƿ�ȡ���ɹ�
     */
    private boolean cancelEntry(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("��������Ҫȡ���ļƻ�����");
        printEntries();
        int opNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opNumber)){
            return false;
        }
        try{
            if(entries.get(opNumber).cancel()){
                logger.info("�ƻ���"+entries.get(opNumber).getPlanningEntryName()+"ȡ���ɹ�");
                return true;
            }
            else {
                throw new IllegalOperationException("���������㣬�޷�ȡ��"+entries.get(opNumber).getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 5.Ϊ�ƻ��������Դ
     * @return �Ƿ����ɹ�
     */
    private boolean allocateSource(){
        System.out.println("��������Ҫ����ļƻ�����");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int inputEntryNumber = scanner.nextInt();

        if(!isLegalEntryNumber(inputEntryNumber)){
            logger.warning("Ϊ�ƻ��������Դʱδѡ��Χ�ڼƻ���");
            return false;
        }

        //�����źϷ��������Ҫ���õļƻ���
        TrainEntry tempEntry = entries.get(inputEntryNumber);
        try{
            if(tempEntry.getState()!= State.WAITING){
                throw new IllegalOperationException("�ƻ���"+tempEntry.getPlanningEntryName()+"�ѷ�����Դ���޷��ٴη���");
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        //�üƻ���δ������Դ
        System.out.println("��������Ҫ���䳵�����Ŀ");
        int trainNumber = scanner.nextInt();

        try{
            if(trainNumber>trains.size()){
                throw new InputOutOfBoundException("���г����������㣬Ϊ�ƻ���"+tempEntry.getPlanningEntryName()+"���䳵��ʧ��");
            }
        }
        catch (InputOutOfBoundException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        List<Train> trainList = new ArrayList<>();
        List<Integer> opList = new ArrayList<>();
        System.out.println("��������Ҫ�������Դ����ţ������ظ�����");
        printTrains();
        for(int i=0 ; i<trainNumber ; i++){
            int opNumber = scanner.nextInt();
            try{
                if(opList.contains(opNumber)){
                    throw new RepeatedAdditionException("�����ظ����ᣬΪ�ƻ���"+tempEntry.getPlanningEntryName()+"���䳵��ʧ��");
                }
            }
            catch (RepeatedAdditionException e){
                logger.warning(e.getMessage());
                e.printStackTrace();
                return false;
            }
            Train tempTrain = trains.get(opNumber);
            trainList.add(tempTrain);
        }

        //��÷����б�����Ƿ������Դ��ͻ
        tempEntry.setSource(trainList);
        try{
            PlanningEntryAPIsStrategy<Train> api = new PlanningEntryAPIsStrategy<>(2);
            if(api.checkResource(entries)){
                throw new AllocateSourceException("����ĳ�����Ѵ��ڼƻ����ͻ��Ϊ�ƻ���"+tempEntry.getPlanningEntryName()+"���䳵��ʧ��");
            }
        }
        catch (AllocateSourceException e){
            //���üƻ��ԭ��δ����״̬
            logger.warning(e.getMessage());
            e.printStackTrace();
            trainList = new ArrayList<>();
            tempEntry.setSource(trainList);
            return false;
        }
        logger.info("Ϊ�ƻ���"+tempEntry.getPlanningEntryName()+"���䳵��ɹ�");
        return true;
    }

    /**
     * 6.����ĳ���ƻ���
     * @return �Ƿ������ɹ�
     */
    private boolean runEntry(){
        System.out.println("��������Ҫ�����ļƻ������");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opEntryNumber)){
            return false;
        }

        TrainEntry opEntry = entries.get(opEntryNumber);
        try{
            if(opEntry.run()){
                logger.info("�����ƻ���"+opEntry.getPlanningEntryName()+"�ɹ�");
                return true;
            }
            else {
                throw new IllegalOperationException("���������㣬�޷������ƻ���"+opEntry.getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 7.����ָ���ƻ���
     * @return �Ƿ����ɹ�
     */
    private boolean blockEntry(){
        System.out.println("��������Ҫ�����ļƻ������");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opEntryNumber)){
            return false;
        }
        TrainEntry opEntry = entries.get(opEntryNumber);
        try{
            if(opEntry.block()){
                logger.info("����ƻ���"+opEntry.getPlanningEntryName()+"�ɹ�");
                return true;
            }
            else {
                throw new IllegalOperationException("���������㣬�޷�����ƻ���"+opEntry.getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 8.����һ���ƻ���
     * @return �Ƿ�����ɹ�
     */
    private boolean endEntry(){
        System.out.println("��������Ҫ�����ļƻ������");
        printEntries();
        Scanner scanner = new Scanner(System.in);
        int opEntryNumber = scanner.nextInt();
        if(!isLegalEntryNumber(opEntryNumber)){
            return false;
        }
        TrainEntry opEntry = entries.get(opEntryNumber);
        try{
            if(opEntry.end()){
                logger.info("�����ƻ���"+opEntry.getPlanningEntryName()+"�ɹ�");
                return true;
            }
            else {
                throw new IllegalOperationException("���������㣬�޷������ƻ���"+opEntry.getPlanningEntryName());
            }
        }
        catch (IllegalOperationException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 9.��ȡ�ƻ���״̬
     * @return �Ƿ��ѯ�ɹ�
     */
    private boolean getEntryState(){
        printEntries();
        Scanner scanner = new Scanner(System.in);
        System.out.println("��������Ҫ��ȡ��Ŀ�ı��");
        int input = scanner.nextInt();
        //������Ƿ�Ϸ�
        if(!isLegalEntryNumber(input)){
            return false;
        }
        else {
            TrainEntry tempEntry = entries.get(input);
            System.out.println("�üƻ����״̬�ǣ�" + tempEntry.getStatusToCharacters());
            logger.info("��ѯ�ƻ���"+tempEntry.getPlanningEntryName()+"��״̬");
            return true;
        }
    }

    /**
     * 10.����Ƿ������Դ��ͻ
     * @return �����Ƿ�ɹ�
     */
    private boolean checkResourceConflict(){
        PlanningEntryAPIsStrategy<Plane> api = new PlanningEntryAPIsStrategy(2);
        if(api.checkResource(entries)){
            System.out.println("������Դ�����ͻ");
        }
        else {
            System.out.println("��������Դ�����ͻ");
        }
        logger.info("���Ŀǰ�ļƻ����Ƿ������Դ��ͻ");
        return true;
    }

    /**
     * 11.��ȡʹ��ָ����Դ�����мƻ���Լ�ָ����Ŀ��ǰ��ƻ���
     * @return �Ƿ�����ɹ�
     */
    private boolean getEntriesUseInputResourceAndPreEntry(){
        printTrains();
        System.out.println("��������Ҫ��ѯ��Դ�����");
        Scanner scanner = new Scanner(System.in);
        int trainNumber = scanner.nextInt();
        if(!isLegalTrainNumber(trainNumber)){
            return false;
        }
        Train targetTrain = trains.get(trainNumber);
        //�����ƻ��Ѱ��ʹ�ø���Դ�ļƻ���
        List<TrainEntry> entriesWithTargetTrain = new ArrayList<>();
        for(TrainEntry tempEntry : entries){
            for(Train tempTrain : tempEntry.getSource()){
                if(tempTrain.equals(targetTrain)){
                    entriesWithTargetTrain.add(tempEntry);
                    break;
                }
            }
        }
        System.out.println("ʹ�ø���Դ�ļƻ�������");
        for(int i=0 ; i<entriesWithTargetTrain.size() ; i++){
            System.out.println(i+"."+entriesWithTargetTrain.get(i).toString());
        }
        System.out.println("��������Ҫ��ѯǰ��ƻ�������");
        int entryNumber = scanner.nextInt();

        try{
            if(entryNumber<0 || entryNumber>=entriesWithTargetTrain.size()){
                throw new InputOutOfBoundException("����ƻ�����ų�����Χ");
            }
        }
        catch (InputOutOfBoundException e){
            logger.warning(e.getMessage());
            e.printStackTrace();
            return false;
        }

        TrainEntry targetEntry = entriesWithTargetTrain.get(entryNumber);
        PlanningEntryAPIsStrategy<Train> api = new PlanningEntryAPIsStrategy<>(2);
        TrainEntry finalEntry = (TrainEntry) api.findPreSameResourceEntry(targetTrain, targetEntry, entries);
        logger.info("��ѯ"+targetEntry.getPlanningEntryName()+"ǰ��ƻ���");
        if(finalEntry==null){
            System.out.println("�ƻ���"+targetEntry.getPlanningEntryName()+"��ǰ��ƻ���");
            return true;
        }
        else {
            System.out.println("ǰ��ƻ�����\n"+finalEntry.toString());
            return true;
        }
    }

    /**
     * 12.��ʾָ��λ�õ���Ϣ��
     * @return �Ƿ���ʾ�ɹ�
     */
    private boolean showInfoBoard(){
        System.out.println("��������Ҫ��ѯ��λ�õı��");
        printLocations();
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(!isLegalLocationNumber(opNumber)){
            return false;
        }
        else {
            logger.info("��ѯ��"+locations.get(opNumber).getLocationName()+"λ�õ���Ϣ��");
            new TrainBoard(locations.get(opNumber).getLocationName(), entries);
            return true;
        }
    }

    /**
     * 13.ɾ��ָ��λ��
     * @return �Ƿ�ɾ���ɹ�
     */
    private boolean deleteLocation(){
        printLocations();
        System.out.println("��������Ҫɾ����λ�ñ��");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(isLegalLocationNumber(opNumber)){
            //����Ƿ����ռ�ø�λ����δ�����ļƻ���
            Location opLocation = locations.get(opNumber);
            for(TrainEntry tempEntry : entries){
                for(Location tempLocation : tempEntry.getLocation()){
                    if(tempLocation.equals(opLocation)){
                        try{
                            if(tempEntry.getState()!=State.ENDED && tempEntry.getState()!=State.CANCELLED){
                                throw new DeleteLocationException("����ռ�ø�λ����δ�����ļƻ���"+tempEntry.getPlanningEntryName());
                            }
                        }
                        catch (DeleteLocationException e){
                            logger.warning(e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
            }
            //������ռ�ø�λ�õ�δ�����ļƻ����ɾ��
            logger.info("ɾ��λ��"+locations.get(opNumber).getLocationName());
            locations.remove(opNumber);
            return true;
        }
        else {
            logger.warning("ָ��ɾ��λ�ó�����Χ");
            return false;
        }
    }

    /**
     * 14.ɾ��ָ����Դ
     * @return �Ƿ�ɾ���ɹ�
     */
    private boolean deleteTrain(){
        printTrains();
        System.out.println("��������Ҫɾ���ĳ�����");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(isLegalTrainNumber(opNumber)){
            //����Ƿ����ռ�øó�����δ�����ļƻ���
            Train opTrain = trains.get(opNumber);
            for(TrainEntry tempEntry : entries){
                if(tempEntry.getSource().size()==0){
                    continue;
                }
                for(Train tempTrain : tempEntry.getSource()){
                    if(tempTrain.equals(opTrain)){
                        try{
                            if(tempEntry.getState()!=State.CANCELLED && tempEntry.getState()!=State.ENDED){
                                throw new DeleteSourceException("����ռ�øó�����δ�����ļƻ���"+tempEntry.getPlanningEntryName());
                            }
                        }
                        catch (DeleteSourceException e){
                            logger.warning(e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
            }
            //������ռ�øó�����δ�����ļƻ����ֱ��ɾ��
            logger.info("ɾ������"+opTrain.getNumber()+"�ɹ�");
            trains.remove(opNumber);
            return true;
        }
        else {
            logger.warning("ָ��ɾ�����ᳬ����Χ");
            return false;
        }
    }

    /**
     * 15.����ɸѡ������ʾ��־
     * @return �Ƿ�����ɹ�
     */
    private boolean showLog(){
        try{
            bfr = new BufferedReader(new FileReader("src/P2/TrainScheduleLog.txt"));
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        //֧�ְ�����Ϣ���͡��������͡�ʱ�����
        System.out.println("�����밴�պ�����������");
        System.out.println("1.������Ϣ����");
        System.out.println("2.���ղ�������");
        System.out.println("3.����ʱ��");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextInt()){
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
     * @return �Ƿ���ҳɹ�
     */
    private boolean searchByInfoType(){
        System.out.println("��������Ҫ���ҵ���Ϣ����: 1.INFO 2.WARNING 3.SEVERE");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(opNumber!=1 && opNumber!=2 && opNumber!=3){
            System.out.println("������Ϣ���������²���");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P2\\.(.*?)\\n([A-Z]+): (.*?)");
        while (opLogString!=null){
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if(opNumber==1 && matcher.group(3).equals("INFO")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\tλ�ã�"+matcher.group(2)+"\t���ݣ�"+matcher.group(4));
            }
            else if(opNumber==2 && matcher.group(3).equals("WARNING")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\tλ�ã�"+matcher.group(2)+"\t���ݣ�"+matcher.group(4));
            }
            else if(opNumber==3 && matcher.group(3).equals("SEVERE")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\tλ�ã�"+matcher.group(2)+"\t���ݣ�"+matcher.group(4));
            }
            opLogString = readSingleLog();
        }
        return true;
    }

    /**
     * 15.������־�������Ͳ���
     * @return �Ƿ���ҳɹ�
     */
    private boolean searchByOperationType(){
        System.out.println("��������Ҫ���ҵ���Ϣ����:");
        System.out.println("��ӭʹ�ó�վ��������������Ӧ�����ɲ���");
        System.out.println("1.����һ������");
        System.out.println("2.����һ���ص�");
        System.out.println("3.����һ���ƻ���");
        System.out.println("4.ȡ��ĳ���ƻ���");
        System.out.println("5.Ϊĳ���ƻ��������Դ");
        System.out.println("6.����ĳ���ƻ���");
        System.out.println("7.����ĳ���ƻ���");
        System.out.println("8.����ĳ���ƻ���");
        System.out.println("9.��ѯĳ�ƻ����״̬");
        System.out.println("10.����Ѵ��ڼƻ����Ƿ������Դ��ͻ");
        System.out.println("11.��ȡʹ��ָ����Դ�����мƻ���Լ�ָ����Ŀ��ǰ��ƻ���");
        System.out.println("12.��ʾָ��λ�ó�վ����Ϣ��");
        System.out.println("13.ɾ��ָ��λ��");
        System.out.println("14.ɾ��ָ������");
        Scanner scanner = new Scanner(System.in);
        int opNumber = scanner.nextInt();
        if(opNumber<1 || opNumber>14){
            System.out.println("������Ϣ���������²���");
            return false;
        }

        String opLogString = readSingleLog();
        Pattern pattern = Pattern.compile("(.*?)P2\\.([A-Z||a-z]+)\\s([A-Z||a-z]+)\\n([A-Z]+): (.*?)");
        while (opLogString!=null) {
            Matcher matcher = pattern.matcher(opLogString);
            matcher.matches();
            if(opNumber==1 && matcher.group(3).equals("addTrain")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==2 && matcher.group(3).equals("addLocation")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==3 && matcher.group(3).equals("addEntry")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==4 && matcher.group(3).equals("cancelEntry")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==5 && matcher.group(3).equals("allocateSource")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==6 && matcher.group(3).equals("runEntry")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==7 && matcher.group(3).equals("blockEntry")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==8 && matcher.group(3).equals("endEntry")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==9 && matcher.group(3).equals("getEntryState")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==10 && matcher.group(3).equals("checkResourceConflict")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==11 && matcher.group(3).equals("getEntriesUseInputResourceAndPreEntry")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==12 && matcher.group(3).equals("showInfoBoard")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==13 && matcher.group(3).equals("deleteLocation")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            else if(opNumber==14 && matcher.group(3).equals("deleteTrain")){
                System.out.println("ʱ�䣺"+matcher.group(1)+"\t������"+matcher.group(3)+"\t���ݣ�"+matcher.group(5));
            }
            opLogString = readSingleLog();
        }
        return true;
    }

    /**
     * 15.ÿ�ζ�ȡ��־��һ����У�
     * @return ��ȡ������
     */
    private String readSingleLog(){
        try{
            String stringToReturn = bfr.readLine();
            if(stringToReturn == null){
                return null;
            }
            else {
                stringToReturn += "\n"+bfr.readLine();
                return stringToReturn;
            }
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
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
        if ((!isLegalTimeFormat(startTime)) || (!isLegalTimeFormat(endTime))) {
            return false;
        }
        if (TimePair.compareTime(startTime, endTime) != 1) {
            System.out.println("������ʼʱ����ڻ�������ֹʱ��");
            return false;
        }

        Pattern pattern = Pattern.compile("(20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}):\\d{2}\\sP2\\.(.*?)\\n(.*?)");
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
     * �������ʱ���Ƿ�����yyyy-MM-dd HH:mm�ĸ�ʽ
     * @param inputTime ����ʱ��
     * @return �Ƿ������ʽ
     */
    private boolean isLegalTimeFormat(String inputTime){
        Pattern pattern = Pattern.compile("20\\d{2}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        Matcher matcher = pattern.matcher(inputTime);
        try{
            if(matcher.matches()){
                return true;
            }
            else {
                throw new WrongTimeFormatException("����ʱ�䲻����yyyy-MM-dd HH:mm��ʽ");
            }
        }
        catch (WrongTimeFormatException e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * ��ʾ����ӵĵص�
     */
    private void printLocations(){
        for(int i=0 ; i<locations.size() ; i++){
            Location temp = locations.get(i);
            System.out.println(i+"."+temp.getLocationName());
        }
    }

    /**
     * ��ʾ����Ӽƻ���
     */
    private void printEntries(){
        for(int i=0 ; i<entries.size() ; i++){
            TrainEntry temp = entries.get(i);
            System.out.println(i+"."+temp.toString());
        }
    }

    /**
     * ��ʾ����ӳ���
     */
    private void printTrains(){
        for(int i=0 ; i<trains.size() ; i++){
            Train temp = trains.get(i);
            System.out.println(i+"."+temp.toString());
        }
    }

    /**
     * ������������Ƿ���entries��Χ��
     * @param num ��Ҫ��������
     * @return �Ƿ��ڷ�Χ��
     */
    private boolean isLegalEntryNumber(int num){
        try{
            if(num>=0 && num<entries.size()){
                return true;
            }
            else{
                throw new InputOutOfBoundException("����ƻ����ų�����Χ");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ������������Ƿ���trains��Χ��
     * @param num ��Ҫ��������
     * @return �Ƿ��ڷ�Χ��
     */
    private boolean isLegalTrainNumber(int num){
        try{
            if(num>=0 && num<trains.size()){
                return true;
            }
            else{
                throw new InputOutOfBoundException("���복���ų�����Χ");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ������������Ƿ���locations�ķ�Χ��
     * @param num ��Ҫ��������
     * @return �Ƿ���locations��Χ��
     */
    private boolean isLegalLocationNumber(int num){
        try{
            if(num>=0 && num<locations.size()){
                return true;
            }
            else{
                throw new InputOutOfBoundException("����λ�ñ�ų�����Χ");
            }
        }
        catch (InputOutOfBoundException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ��ʾ�˵�ѡ��
     */
    private void printMenu(){
        System.out.println("��ӭʹ�ó�վ��������������Ӧ�����ɲ���");
        System.out.println("1.����һ������");
        System.out.println("2.����һ���ص�");
        System.out.println("3.����һ���ƻ���");
        System.out.println("4.ȡ��ĳ���ƻ���");
        System.out.println("5.Ϊĳ���ƻ��������Դ");
        System.out.println("6.����ĳ���ƻ���");
        System.out.println("7.����ĳ���ƻ���");
        System.out.println("8.����ĳ���ƻ���");
        System.out.println("9.��ѯĳ�ƻ����״̬");
        System.out.println("10.����Ѵ��ڼƻ����Ƿ������Դ��ͻ");
        System.out.println("11.��ȡʹ��ָ����Դ�����мƻ���Լ�ָ����Ŀ��ǰ��ƻ���");
        System.out.println("12.��ʾָ��λ�ó�վ����Ϣ��");
        System.out.println("13.ɾ��ָ��λ��");
        System.out.println("14.ɾ��ָ������");
        System.out.println("15.������־");
        System.out.println("16.�˳�����");
    }

    public static void main(String argv[]){
        new TrainScheduleApp().runApp();
    }
}

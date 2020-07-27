package debug;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LowestPriceTest {
    /**
     * 检测输入非法数据
     * 输入划分：1.prices超出限制；2.special offers超出限制；3.needs超出限制；4.输入参数不兼容
     */
    @Test
    public void testIllegalInputs(){
        LowestPrice test = new LowestPrice();

        //测试非法数据1：物品数超出限制
        List<Integer> price1 = new ArrayList<>();
        for(int i=1 ; i<8 ; i++){
            price1.add(i);
        }
        List<List<Integer>> offer1 = new ArrayList<>();
        List<Integer> tempOffer = new ArrayList<>();
        tempOffer.add(2);
        tempOffer.add(3);
        offer1.add(tempOffer);
        List<Integer> needs1 = new ArrayList<>();
        needs1.add(9);
        assertEquals(-1, test.shoppingOffers(price1, offer1, needs1));

        //测试非法数据2：special offer数超出限制
        List<Integer> price2 = new ArrayList<>();
        price2.add(1);
        List<List<Integer>> offer2 = new ArrayList<>();
        for(int i=0 ; i<103 ; i++){
            List<Integer> temp = new ArrayList<>();
            temp.add(i);
            temp.add(i+1);
            offer2.add(temp);
        }
        List<Integer> needs2 = new ArrayList<>();
        needs1.add(9);
        assertEquals(-1, test.shoppingOffers(price2, offer2, needs2));

        //测试非法数据3：needs数超出限制
        List<Integer> price3 = new ArrayList<>();
        price3.add(2);
        List<List<Integer>> offer3 = new ArrayList<>();
        List<Integer> tempOffer3 = new ArrayList<>();
        tempOffer.add(2);
        tempOffer.add(3);
        offer3.add(tempOffer);
        List<Integer> needs3 = new ArrayList<>();
        for(int i=1 ; i<8 ; i++){
            needs3.add(i);
        }
        assertEquals(-1, test.shoppingOffers(price3, offer3, needs3));

        //测试非法数据4：三个输入参数不兼容
        List<Integer> price4 = new ArrayList<>();
        price4.add(2);
        List<List<Integer>> offer4 = new ArrayList<>();
        List<Integer> tempOffer4 = new ArrayList<>();
        tempOffer.add(2);
        tempOffer.add(3);
        offer3.add(tempOffer4);
        List<Integer> needs4 = new ArrayList<>();
        for(int i=1 ; i<3 ; i++){
            needs3.add(i);
        }
        assertEquals(-1, test.shoppingOffers(price4, offer4, needs4));
    }


    /**
     * 测试方法的正确性
     * 输入划分：正常数据；输入可能使总价更低但是购买额外物品的数据；使用special offer价格更高的数据
     */
    @Test
    public void testShopping(){
        LowestPrice test = new LowestPrice();
        //1.正常数据 Input: [2,5], [[3,0,5],[1,2,10]], [3,2] Output: 14
        List<Integer> price1 = new ArrayList<>();
        price1.add(2);
        price1.add(5);
        List<List<Integer>> offers1 = new ArrayList<>();
        List<Integer> offer11 = new ArrayList<>();
        offer11.add(3);
        offer11.add(0);
        offer11.add(5);
        List<Integer> offer12 = new ArrayList<>();
        offer12.add(1);
        offer12.add(2);
        offer12.add(10);
        offers1.add(offer11);
        offers1.add(offer12);
        List<Integer> needs1 = new ArrayList<>();
        needs1.add(3);
        needs1.add(2);
        assertEquals(14, test.shoppingOffers(price1, offers1, needs1));
        //2.输入可能使总价更低但是购买额外物品的数据 [7,8], [[5,5,7],[1,2,5]], [3,3] Output:27
        List<Integer> price2 = new ArrayList<>();
        price2.add(7);
        price2.add(8);
        List<List<Integer>> offers2 = new ArrayList<>();
        List<Integer> offer21 = new ArrayList<>();
        offer21.add(5);
        offer21.add(5);
        offer21.add(7);
        List<Integer> offer22 = new ArrayList<>();
        offer22.add(1);
        offer22.add(2);
        offer22.add(5);
        offers2.add(offer21);
        offers2.add(offer22);
        List<Integer> needs2 = new ArrayList<>();
        needs2.add(3);
        needs2.add(3);
        assertEquals(27, test.shoppingOffers(price2, offers2, needs2));
        //3.使用special offer价格更高的数据[5,6], [[1,1,15]], [3,3]
        List<Integer> price3 = new ArrayList<>();
        price3.add(5);
        price3.add(6);
        List<List<Integer>> offers3 = new ArrayList<>();
        List<Integer> offer31 = new ArrayList<>();
        offer31.add(1);
        offer31.add(1);
        offer31.add(15);
        offers3.add(offer31);
        List<Integer> needs3 = new ArrayList<>();
        needs3.add(3);
        needs3.add(3);
        assertEquals(33, test.shoppingOffers(price3, offers3, needs3));
    }
}

// BasicItemController
// 상품목록 - 타임리프

package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";

    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));

    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }
    // addItemV1 - BasicItemController에 추가
    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                            @RequestParam int price,
                            @RequestParam Integer quantity,
                            Model model) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    //  먼저 @RequestParam String itemName: itemName 요청 파라미터데이터를 해당 변수에 받는다.
    //  Item객체를 생성하고 itemRepository를 통해서 저장한다.
    //  저장된 item을 모델에 담아서 뷰에 전달한다.
    //  중요: 여기서는 상품 상세에서 사용한 item.html 뷰 템플릿을 그대로 재활용한다.
    }

    /** addItemV2 - 상품등록처리 - ModelAttribute
     * @ModelAttribute("item") Item item
     * model.addAttribute("item", item); 자동 추가  */
    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);
        //model.addAttribute("item", item); //자동 추가, 생략 가능
        return "basic/item";

        //  @ModelAttribute - 요청파라미터처리
        //  @ModelAttribute는Item객체를생성하고,
        //  요청파라미터의값을프로퍼티접근법(setXxx)으로 입력해준다.
    }

    /** addItemV3 - 상품등록처리 - ModelAttribute 이름생략
     * @ModelAttribute name 생략 가능
     * model.addAttribute(item); 자동 추가, 생략 가능
     * 생략시 model에 저장되는 name은 클래스명 첫글자만 소문자로 등록 Item -> item  */
    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        return "basic/item";
        //  @ModelAttribute의이름을생략할수있다.
        //  주의
        //  @ModelAttribute의 이름을 생략하면 모델에 저장 될 때 클래스명을 사용한다.
        //  이때 클래스의 첫 글자만 소문자로 변경해서 등록한다.
        //  예) @ModelAttribute 클래스명 모델에자동추가되는이름
        //  Item --> item
        //  HelloWorld -->  helloWorld
    }

    /** addItemV4 - 상품등록처리 - ModelAttribute 전체생략
     * @ModelAttribute 자체 생략 가능
     * model.addAttribute(item) 자동 추가  */
    @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
        //  @ModelAttribute 자체도생략가능하다. 대상객체는모델에자동등록된다.
        //  나머지사항은기존과 동일하다.
    }
    //  상품 수정 폼 컨트롤러 , BasicItemController에 추가
    //  수정에 필요한 정보를 조회하고, 수정 용폼 뷰를 호출한다.
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }
    //  상품 수정 개발
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
        //  상품 수정은 상품등록과 전체프로세스가 유사하다.
        //  GET  /items/{itemId}/edit: 상품수정폼
        //  POST /items/{itemId}/edit: 상품수정처리

        //  리 다이렉트
        //  상품 수정은 마지막에 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리 다이렉트를 호출한다.
        //  스프링은 redirect:/...으로 편리하게 리 다이렉트를 지원한다. redirect:/basic/items/{itemId}
        //  컨트롤러에 매핑된@PathVariable의 값은 redirect에도 사용할 수 있다.
    }
    /**
     * RedirectAttributes  */
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";

        //  리다이렉트 할 때 간단히 status=true를 추가 해보자. 그리고 뷰템플릿에서 이 값이 있으면, 저장되었습니다. 라는 메시지를 출력해보자.

        //  실행 해보면 다음과 같은 리다이렉트 결과가 나온다.
        //  http://localhost:8080/basic/items/3?status=true

        //  RedirectAttributes
        //  RedirectAttributes를 사용하면 URL 인코딩도해주고, pathVarible, 쿼리파라미터까지처리해준다.
        //  redirect:/basic/items/{itemId}
        //  pathVariable 바인딩: {itemId}
        //  나머지는쿼리파라미터로처리: ?status=true
    }
}

// 컨트롤러로직은 itemRepository에서모든상품을조회한다음에모델에담는다.
// 그리고뷰템플릿을 호출한다.

//  타임리프 간단히 알아보기
//  타임리프 사용 선언
//  <html xmlns:th="http://www.thymeleaf.org">

//  속성변경 - th:href
//  th:href="@{/css/bootstrap.min.css}"
//  href="value1"을 th:href="value2"의 값으로 변경한다.
//  타임리프 뷰 템플릿을 거치게 되면 원래 값을th:xxx 값으로변경한다. 만약값이없다면새로생성한다. HTML을그대로볼때는href 속성이사용되고, 뷰템플릿을거치면th:href의값이href로
//  대체되면 서동적으로 변경할 수 있다.

//  대부분의 HTML 속성을th:xxx로 변경할 수 있다.

//  타임리프핵심
//  핵심은 th:xxx가 붙은 부분은 서버사이드에서 렌더링 되고, 기존것을대체한다.
//  th:xxx이 없으면 기존 html의 xxx속성이 그대로 사용된다.
//  HTML을 파일로 직접 열었을 때, th:xxx가 있어도 웹브라우저는 th:속성을 알지 못하므로 무시한다.
//  따라서 HTML을 파일보기를 유지하면서 템플릿 기능도 할 수 있다.

//  URL 링크표현식 - @{...},
//  th:href="@{/css/bootstrap.min.css}"

//  상품등록폼으로이동 속성변경 - th:onclick
//  onclick="location.href='addForm.html'"
//  th:onclick="|location.href='@{/basic/items/add}'|"

//  리터럴대체 - |...|
//  |...|:이렇게사용한다.
//  타임리프에서문자와표현식등은분리되어있기때문에더해서사용해야한다.
//  <span th:text="'Welcome to our application, ' + ${user.name} + '!'">

//  반복출력 - th:each
//  <tr th:each="item : ${items}">

//  변수표현식 - ${...}
//  <td th:text="${item.price}">10000</td>

//  내용변경 - th:text
//  <td th:text="${item.price}">10000</td>

//  URL 링크표현식2 - @{...},
//  th:href="@{/basic/items/{itemId}(itemId=${item.id})}"

//  URL 링크간단히
//  th:href="@{|/basic/items/${item.id}|}"

//  참고
//  타임리프는 순수 HTML 파일을 웹 브라우저에서 열어도 내용을 확인할 수 있고, 서버를 통해 뷰 템플릿을 거치면 동적으로 변경된 결과를 확인 할 수 있다.
//  JSP를 생각해보면, JSP 파일은 웹 브라우저에서 그냥 열면 JSP 소스코드와 HTML이 뒤죽박죽 되어서 정상적인 확인이 불 가능하다.
//  오직 서버를 통해서 JSP를 열어야 한다.
//  이렇게 순수 HTML을 그대로 유지하면서 뷰 템플릿도 사용할 수 있는 타임리프의 특징을 네츄럴 템플릿 (natural templates)이라한다.

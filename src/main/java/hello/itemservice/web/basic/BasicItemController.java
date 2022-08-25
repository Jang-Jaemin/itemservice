// BasicItemController
// 상품목록 - 타임리프

package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

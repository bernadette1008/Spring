package kr.ac.poly.ex2.repository;

import kr.ac.poly.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1, 100).forEach(i->{
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        });
    }

    @Test
    public void testSelect(){
        Long mno = 10L;
        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("==============================");
        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 50L;

        Memo memo = memoRepository.getOne(mno);

        System.out.println("===============================");
        System.out.println(memo);
    }

    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(100).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete(){
        Long mno = 101L;

        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(0,10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);

        System.out.println("----------------------------");
        System.out.println("Total Pages:"+result.getTotalPages());
        System.out.println("Total Count:"+result.getTotalElements());
        System.out.println("Page Number:"+result.getNumber());
        System.out.println("Page Size:"+result.getSize());
        System.out.println("has Next Page?:"+result.hasNext());
        System.out.println("has first page?:"+result.isFirst());
        System.out.println("----------------------------");
        for(Memo memo : result.getContent()){
            System.out.println(memo);
        }
    }

    @Test
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0,10,sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    @Test
    public void testQueryMethods(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for(Memo memo : list){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("Mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods(){
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void testupdateMemoText() {
        int updateCount = memoRepository.updateMemoText(30L, "mno가 30인 내용을 수정한다");
    }

    @Test
    public void testupdateMemoText2() {
        Memo memo = new Memo();
        memo.setMno(31);
        memo.setMemoText("31행 수정 Memo객체 참조갑을 param으로 사용");
        int updateCount = memoRepository.updateMemoText2(memo);
    }

    @Test
    public void testGetListWithQuery(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").ascending());
        Page<Memo> result = memoRepository.getListWithQuery(32L, pageable);

        result.get().forEach(
                memo -> System.out.println(memo)
        );
    }
}

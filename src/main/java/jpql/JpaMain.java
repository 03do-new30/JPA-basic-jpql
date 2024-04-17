package jpql;

import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        //code
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 영속성 컨텍스트 비운다
            em.flush();
            em.clear();

            // 스칼라타입
            // Object[] 타입으로 조회
            List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();
            Object[] result = resultList.get(0);
            System.out.println("result[0] username = " + result[0]);
            System.out.println("result[1] age = " + result[1]);

            // DTO 만들고, new 명령어로 조회
            List<MemberDTO> resultList1 = em.createQuery(
                    "select distinct new jpql.MemberDTO(m.username, m.age) from Member m",
                            MemberDTO.class)
                    .getResultList();
            System.out.println("resultList1.get(0).getUsername() = " + resultList1.get(0).getUsername());
            System.out.println("resultList1.get(0).getAge() = " + resultList1.get(0).getAge());

            tx.commit(); // 커밋 시점에 INSERT (버퍼링 가능)
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close(); // 영속성 컨텍스트를 종료
        }
        emf.close();
    }
}

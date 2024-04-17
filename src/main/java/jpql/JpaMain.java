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

            // 아래처럼 데이터를 가져오면 이 가져온 Member는 영속성 컨텍스트에 관리가 될까?
            List<Member> result = em.createQuery("select m from Member m", Member.class)
                    .getResultList();
            Member findMember = result.get(0);
            // DB에 변경사항이 반영되면 영속성 컨텍스트에 관리되는거고
            // 반영되지 않으면 관리되지 않는 것이다
            findMember.setAge(20);
            // update쿼리가 나간다! = 관리된다
            // 프로젝션하는 값들이 다 영속성 컨텍스트에서 관리된다고 볼 수 있다

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

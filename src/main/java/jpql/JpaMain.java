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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            member.setTeam(team);

            em.persist(member);

            // 영속성 컨텍스트 비운다
            em.flush();
            em.clear();

            // 페이징 쿼리
            String query = "select m from Member m inner join m.team t"; // inner join
//            String query = "select m from Member m left outer join m.team t"; // left outer join
//            String query = "select m from Member m, Team t where m.username = t.name"; // 세타 조인
            List<Member> resultList = em.createQuery(query, Member.class).getResultList();

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

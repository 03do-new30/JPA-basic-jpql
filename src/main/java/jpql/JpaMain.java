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
            member.setMemberType(MemberType.ADMIN);

            Member member2 = new Member();
            member2.setUsername("관리자");
            member2.setAge(20);

            em.persist(member);
            em.persist(member2);

            // 영속성 컨텍스트 비운다
            em.flush();
            em.clear();

            // concat
            String query = "select concat('a', 'b') from Member m";
            List<String> resultList = em.createQuery(query).getResultList();
            // locate
            String query2 = "select locate('de', 'abcdefg')from Member m"; // 4를 반환함
            List<Integer> resultList2 = em.createQuery(query2).getResultList();
            for (Integer i : resultList2) {
                System.out.println("locate = " + i);
            }
            // size
            String query3 = "select size(t.members) from Team t"; // 크기를 반환
            List<Integer> resultList3 = em.createQuery(query3).getResultList();
            for (Integer i : resultList3) {
                System.out.println("size = " + i);
            }


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

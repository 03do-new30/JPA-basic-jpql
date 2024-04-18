package jpql;

import jakarta.persistence.*;

import java.util.Collection;
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

            // 상태 필드
            String query1 = "select m.username from Member m";
            // 단일값 연관 필드
            // team.name도 갈 수 있다
            // 묵시적인 내부 조인이 발생한다
            String query2 = "select m.team from Member m";
            // 컬렉션값 연관 필드
            // 묵시적인 내부 조인이 발생한다
            // t.members.username 으로 탐색할 수 없다. 컬렉션으로 받아오기 때문에
            String query3 = "select t.members from Team t";
            List<Collection> result = em.createQuery(query3, Collection.class).getResultList();
            tx.commit(); // 커밋 시점에 INSERT (버퍼링 가능)
            for (Object o : result) {
                System.out.println("o = " + o);
            }
            // t.members.username 가져오고싶으면 명시적 join 사용해야 한다
            String query4 = "select m.username from Team t join t.members m";
            List<String> resultList = em.createQuery(query4, String.class).getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close(); // 영속성 컨텍스트를 종료
        }
        emf.close();
    }
}

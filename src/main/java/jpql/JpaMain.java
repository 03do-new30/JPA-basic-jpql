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

            // 기본 CASE식
            String query = "select " +
                    "case when m.age <= 10 then '학생요금' " +
                    "when m.age >= 60 then '경로요금' " +
                    "else 'common' " +
                    "end " +
                    "from Member m";
            List<String> resultList = em.createQuery(query).getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
            }
            // COALESCE - 사용자 이름이 없으면 이름 없는 회원을 반환한다
            String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m";
            List<String> resultList2 = em.createQuery(query2).getResultList();
            for (String s : resultList2) {
                System.out.println("coalesce 결과 = " + s);
            }
            // NULLIF - 두 값이 같으면 null을 반환하고 아니면 본인의 데이터를 반환
            String query3 = "select NULLIF(m.username, '관리자') from Member m";
            List<String> resultList3 = em.createQuery(query3).getResultList();
            for (String s : resultList3) {
                System.out.println("nullif 결과 = " + s);
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

package info.kgeorgiy.ja.razinkov.student;

import info.kgeorgiy.java.advanced.student.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StudentDB implements AdvancedQuery {

    private static final Comparator<Student> STUDENT_COMPARATOR = Comparator
            .comparing(Student::firstName)
            .thenComparing(Student::lastName)
            .thenComparing(Student::id);

    private static final Comparator<Group> GROUP_COMPARATOR = Comparator
            .comparingInt((Group v) -> v.students().size())
            .thenComparing(Group::name);

    private <U> Stream<U> mapStudents(List<Student> students, Function<Student, U> mapper) {
        return students.stream()
                .map(mapper);
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return mapStudents(students, Student::firstName).toList();
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return mapStudents(students, Student::lastName).toList();

    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return mapStudents(students, getStudentFullName()).toList();
    }

    @Override
    public List<GroupName> getGroupNames(List<Student> students) {
        return mapStudents(students, Student::groupName).toList();
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return mapStudents(students, Student::firstName).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream()
                .max(Comparator.naturalOrder())
                .map(Student::firstName)
                .orElse("");
    }

    private List<Student> sortStudentsBy(Collection<Student> students, Comparator<Student> comp) {
        return students.stream()
                .sorted(comp)
                .toList();
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsBy(students, Comparator.naturalOrder());
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsBy(students, STUDENT_COMPARATOR);
    }

    private <T> Stream<Student> filterStudentsBy(Collection<Student> students, Function<Student, T> pred, T filter) {
        return students.stream()
                .filter(s -> pred.apply(s).equals(filter))
                .sorted(STUDENT_COMPARATOR);
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return filterStudentsBy(students, Student::firstName, name).toList();
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return filterStudentsBy(students, Student::lastName, name).toList();
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return filterStudentsBy(students, Student::groupName, group).toList();
    }


    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return filterStudentsBy(students, Student::groupName, group)
                .collect(Collectors.toMap(
                        Student::lastName,
                        Student::firstName,
                        BinaryOperator.minBy(String::compareTo)));
    }

    private Stream<Group> getGroupsBy(Collection<Student> students, Comparator<Student> comp) {
        return students.stream()
                .sorted(comp)
                .collect(Collectors.groupingBy(Student::groupName))
                .entrySet()
                .stream()
                .map(v -> new Group(v.getKey(), v.getValue()))
                .sorted(Comparator.comparing(Group::name));
    }


    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroupsBy(students, STUDENT_COMPARATOR).toList();
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroupsBy(students, Comparator.comparing(Student::id)).toList();
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return getGroupsBy(students, STUDENT_COMPARATOR)
                .max(GROUP_COMPARATOR)
                .map(Group::name)
                .orElse(null);
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return getGroupsByName(students).stream()
                .min(Comparator
                        .comparingLong((Group v) -> v.students().stream()
                                .map(Student::firstName)
                                .distinct()
                                .count())
                        .reversed()
                        .thenComparing(Group::name))
                .map(Group::name)
                .orElse(null);
    }

    private String getPopularName(Collection<Group> groups, int reverseCoef) {
        return groups.stream()
                .map(gr -> gr.students().stream()
                        .map(Student::firstName)
                        .collect(Collectors.toSet())
                )
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet()
                .stream()
                .min(Comparator
                        .comparing((Map.Entry<String, Long> v) -> v.getValue() * reverseCoef)
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey)
                .orElse("");
    }

    @Override
    public String getMostPopularName(Collection<Group> groups) {
        return getPopularName(groups, -1);
    }

    @Override
    public String getLeastPopularName(Collection<Group> groups) {
        return getPopularName(groups, 1);
    }

    private <T> List<T> getByIndices(Collection<Group> groups, int[] indices, Function<Student, T> mapper) {
        return groups.stream()
                .map(Group::students)
                .map(v -> Arrays.stream(indices)
                        .filter(ind -> (ind < v.size() && ind >= 0))
                        .mapToObj(v::get).toList())
                .flatMap(Collection::stream)
                .map(mapper)
                .toList();
    }

    @Override
    public List<String> getFirstNames(Collection<Group> groups, int[] indices) {
        return getByIndices(groups, indices, Student::firstName);
    }

    @Override
    public List<String> getLastNames(Collection<Group> groups, int[] indices) {
        return getByIndices(groups, indices, Student::lastName);
    }

    @Override
    public List<GroupName> getGroupNames(Collection<Group> groups, int[] indices) {
        return getByIndices(groups, indices, Student::groupName);
    }

    @Override
    public List<String> getFullNames(Collection<Group> groups, int[] indices) {
        return getByIndices(groups, indices, getStudentFullName());
    }

    private static Function<Student, String> getStudentFullName() {
        return s -> s.firstName() + " " + s.lastName();
    }
}

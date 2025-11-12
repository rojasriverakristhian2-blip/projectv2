import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Acta } from './acta';

describe('Acta', () => {
  let component: Acta;
  let fixture: ComponentFixture<Acta>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Acta]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Acta);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
